package com.anwera64.pagodividido.newexpenditure

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.base.AppTheme
import com.anwera64.pagodividido.base.compose.BaseTopAppBar
import com.anwera64.pagodividido.newexpenditure.utils.NewExpenseErrorModel
import com.anwera64.pagodividido.newexpenditure.utils.NewExpenseErrorStates
import com.anwera64.pagodividido.newexpenditure.utils.PaymentOptions
import com.anwera64.pagodividido.trip.CompanionSelector
import com.anwera97.domain.models.CompanionModel
import java.util.*

private val tabs = listOf(R.string.details, R.string.payment_info)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewExpenditureContent(
    companionList: List<CompanionModel>,
    backNavigation: () -> Unit,
    payerChosen: Int? = null,
    paymentOption: PaymentOptions? = null,
    errorModel: NewExpenseErrorModel? = null,
    onAmountSet: (Pair<Int, Double>) -> Unit,
    onPayerChosen: (id: Int) -> Unit,
    onOptionChanged: (option: PaymentOptions) -> Unit,
    createExpense: (detail: String?, amountString: String) -> Unit,
    startingTab: Int = indexOfDetailsTab(),
) {
    var selectedTabIndex by remember { mutableStateOf(startingTab) }
    var amountString: String by remember { mutableStateOf("") }
    var detailText: String by remember { mutableStateOf("") }
    val selectedPayer = companionList.find { companion -> companion.uid == payerChosen.toString() }
    var selectedPayerText: String by remember { mutableStateOf(selectedPayer?.name.orEmpty()) }
    Scaffold(
        topBar = {
            BaseTopAppBar(
                backNavigation = {
                    if (selectedTabIndex == indexOfDetailsTab()) {
                        backNavigation()
                    } else {
                        selectedTabIndex = indexOfDetailsTab()
                    }
                },
                title = stringResource(id = R.string.new_expense),
                actions = {
                    IconButton(onClick = { createExpense(detailText, amountString) }) {
                        Icon(Icons.Default.Add, stringResource(id = R.string.create))
                    }
                }
            )
        },
        bottomBar = {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, titleRes ->
                    Tab(
                        text = { Text(stringResource(id = titleRes)) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            val iconRes = when (index) {
                                indexOfDetailsTab() -> R.drawable.ic_info
                                indexOfPaymentsTab() -> R.drawable.ic_money
                                else -> throw Exception("Unsupported index")
                            }
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        val contentPaddingModifier = Modifier.padding(contentPadding)
        if (selectedTabIndex == indexOfDetailsTab()) {
            FirstForm(
                modifier = contentPaddingModifier,
                companionList = companionList,
                amountString = amountString,
                detailText = detailText,
                selectorText = selectedPayerText,
                errorModel = errorModel,
                onDetailChanged = { newValue -> detailText = newValue },
                onSelectorChanged = { selectedCompanion ->
                    selectedPayerText = selectedCompanion.name
                    val companionId = selectedCompanion.uid.toInt()
                    onPayerChosen(companionId)
                },
                onAmountChanged = { newAmountString -> amountString = newAmountString }
            )
        } else {
            PaymentInfo(
                errorModel = errorModel,
                modifier = contentPaddingModifier,
                companionList = companionList,
                totalAmount = amountString.toDoubleOrNull() ?: 0.0,
                selectedOption = paymentOption,
                onOptionChanged = onOptionChanged,
                onAmountSet = onAmountSet
            )
        }
    }
}

private fun indexOfDetailsTab() = tabs.indexOf(R.string.details)

private fun indexOfPaymentsTab() = tabs.indexOf(R.string.payment_info)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FirstForm(
    companionList: List<CompanionModel>,
    amountString: String,
    detailText: String,
    selectorText: String,
    modifier: Modifier = Modifier,
    errorModel: NewExpenseErrorModel? = null,
    onAmountChanged: (amount: String) -> Unit,
    onDetailChanged: (amount: String) -> Unit,
    onSelectorChanged: (companion: CompanionModel) -> Unit,
) {
    val scrollState = rememberScrollState()
    val totalAmountHasErrors = errorModel?.totalAmountError != null
    val payerSelectorHasErrors = errorModel?.payerError != null
    Column(
        modifier = modifier.scrollable(state = scrollState, orientation = Orientation.Vertical)
    ) {
        val selectorHint = stringResource(R.string.who_paid)
        OutlinedTextField(
            value = amountString,
            onValueChange = { newValue -> onAmountChanged(newValue) },
            label = { Text(stringResource(id = R.string.amount)) },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            isError = totalAmountHasErrors,
            supportingText = {
                if (totalAmountHasErrors) {
                    Text(stringResource(id = errorModel!!.totalAmountError!!.errorResource))
                }
            }
        )
        CompanionSelector(
            companionList = companionList,
            textFieldValue = selectorText,
            requestCompanionResult = onSelectorChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp),
            label = selectorHint,
            isError = payerSelectorHasErrors,
            errorText = {
                if (payerSelectorHasErrors) {
                    Text(stringResource(id = errorModel!!.payerError!!.errorResource))
                }
            }
        )
        OutlinedTextField(
            value = detailText,
            onValueChange = onDetailChanged,
            label = { Text(stringResource(id = R.string.detail)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp)
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentInfo(
    modifier: Modifier = Modifier,
    companionList: List<CompanionModel>,
    totalAmount: Double,
    selectedOption: PaymentOptions?,
    errorModel: NewExpenseErrorModel? = null,
    onOptionChanged: (option: PaymentOptions) -> Unit,
    onAmountSet: (Pair<Int, Double>) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            val paymentOptions = PaymentOptions.values()
            val paymentWayHasErrors = errorModel?.paymentWayError != null
            PaymentOptionSelector(
                options = paymentOptions,
                selectedOption = selectedOption,
                requestCompanionResult = onOptionChanged,
                isError = paymentWayHasErrors,
                errorText = {
                    if (paymentWayHasErrors) {
                        Text(stringResource(id = errorModel!!.paymentWayError!!.errorResource))
                    }
                }
            )
        }
        if (selectedOption == null) return@LazyColumn
        items(companionList) { companion ->
            PaymentInput(
                selectedOption = selectedOption,
                totalAmount = totalAmount,
                companionList = companionList,
                errorModel = errorModel,
                companion = companion,
                onAmountSet = onAmountSet
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PaymentInput(
    selectedOption: PaymentOptions?,
    totalAmount: Double,
    companionList: List<CompanionModel>,
    errorModel: NewExpenseErrorModel?,
    companion: CompanionModel,
    onAmountSet: (Pair<Int, Double>) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    val equalsEnabled = selectedOption == PaymentOptions.EQUALS
    val textFieldValue = if (equalsEnabled) {
        stringResource(id = R.string.amount_placeholder, totalAmount / companionList.size)
    } else {
        amountText
    }
    val companionId: Int = companion.uid.toInt()
    val errorState: NewExpenseErrorStates? = errorModel?.paymentRelationshipErrors?.get(companionId)
    val errorEnabled: Boolean = errorState != null
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            amountText = newValue
            val amount = newValue.toDoubleOrNull() ?: 0.0
            onAmountSet(companionId to amount)
        },
        readOnly = equalsEnabled,
        enabled = !equalsEnabled,
        label = { Text(text = companion.name) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp),
        isError = errorEnabled,
        supportingText = {
            if (errorEnabled) {
                Text(stringResource(id = errorState!!.errorResource))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentOptionSelector(
    options: Array<PaymentOptions>,
    requestCompanionResult: (option: PaymentOptions) -> Unit,
    selectedOption: PaymentOptions? = null,
    isError: Boolean = false,
    errorText: @Composable () -> Unit = {}
) {
    var isExpanded: Boolean by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
    ) {
        val selectorText = selectedOption?.stringRes?.let { id -> stringResource(id = id) }
        OutlinedTextField(
            value = selectorText.orEmpty(),
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Open option list"
                )
            },
            label = { Text(text = stringResource(R.string.payment_way)) },
            isError = isError,
            supportingText = errorText
        )
        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            options.forEach { option ->
                val optionText = stringResource(id = option.stringRes)
                DropdownMenuItem(
                    text = { Text(optionText) },
                    onClick = {
                        requestCompanionResult(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewDetails() {
    AppTheme {
        NewExpenditureContent(
            backNavigation = {},
            companionList = listOf(
                CompanionModel(
                    uid = UUID.randomUUID().toString(),
                    name = "Zea"
                ),
                CompanionModel(
                    uid = UUID.randomUUID().toString(),
                    name = "Jaz"
                ),
                CompanionModel(
                    uid = UUID.randomUUID().toString(),
                    name = "Tongo"
                )
            ),
            onAmountSet = {},
            onPayerChosen = {},
            onOptionChanged = {},
            createExpense = { _, _ -> }
        )
    }
}

@Composable
@Preview
fun PreviewPaymentInfo() {
    AppTheme {
        NewExpenditureContent(
            backNavigation = {},
            companionList = listOf(
                CompanionModel(
                    uid = UUID.randomUUID().toString(),
                    name = "Zea"
                ),
                CompanionModel(
                    uid = UUID.randomUUID().toString(),
                    name = "Jaz"
                ),
                CompanionModel(
                    uid = UUID.randomUUID().toString(),
                    name = "Tongo"
                )
            ),
            onAmountSet = {},
            onPayerChosen = {},
            onOptionChanged = {},
            createExpense = { _, _ -> },
            startingTab = indexOfPaymentsTab(),
        )
    }
}