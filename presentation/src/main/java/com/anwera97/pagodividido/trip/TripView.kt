package com.anwera97.pagodividido.trip

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.models.ResultModel
import com.anwera97.pagodividido.R
import com.anwera97.pagodividido.base.AppTheme
import com.anwera97.pagodividido.base.compose.BaseTopAppBar
import java.util.Date
import java.util.UUID
import kotlin.math.absoluteValue

private val tabs: List<Int> = listOf(R.string.details, R.string.result)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripView(
    topBarTitle: String,
    backNavigation: () -> Unit,
    expenditures: List<ExpenditureModel>,
    companionList: List<CompanionModel>,
    resultModel: ResultModel?,
    createNewExpenditure: () -> Unit,
    requestCompanionResult: (uid: String) -> Unit,
    startingTab: Int = indexOfDetailsTab()
) {
    var selectedTabIndex by remember { mutableStateOf(startingTab) }
    Scaffold(
        topBar = { BaseTopAppBar(backNavigation, topBarTitle) },
        floatingActionButton = {
            CreatePaymentButton(selectedTabIndex, onClick = createNewExpenditure)
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, titleRes ->
                    Tab(
                        text = { Text(stringResource(id = titleRes)) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            val iconRes = when (index) {
                                0 -> R.drawable.ic_money
                                1 -> R.drawable.ic_people
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
        val paddingModifier = Modifier.padding(contentPadding)
        if (selectedTabIndex == indexOfDetailsTab()) {
            PaymentsList(
                modifier = paddingModifier,
                expenditures = expenditures
            )
        } else {
            ResultPage(
                modifier = paddingModifier,
                companionList = companionList,
                requestCompanionResult = requestCompanionResult,
                resultModel = resultModel
            )
        }
    }
}

@Composable
private fun ResultPage(
    modifier: Modifier = Modifier,
    companionList: List<CompanionModel>,
    requestCompanionResult: (uid: String) -> Unit,
    resultModel: ResultModel?
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            val selectorHint = stringResource(R.string.select_companion)
            var textFieldValue: String by remember { mutableStateOf("") }
            if (resultModel !== null) textFieldValue = resultModel.companion.name
            CompanionSelector(
                companionList = companionList,
                textFieldValue = textFieldValue,
                requestCompanionResult = { companion ->
                    requestCompanionResult(companion.uid)
                    textFieldValue = companion.name
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                label = selectorHint
            )
            TotalSpentText(resultModel)
        }
        items(resultModel?.debts?.toList().orEmpty()) { (companion, amount) ->
            DebtItem(companion.name, amount)
        }
    }
}

@Composable
private fun TotalSpentText(resultModel: ResultModel?) {
    if (resultModel == null) return
    Text(
        text = stringResource(R.string.total_spent, resultModel.totalPaid),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun DebtItem(
    name: String,
    amount: Double,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        val nameBody: String = when {
            amount > 0 -> stringResource(R.string.owes_you, name)
            amount == 0.0 -> stringResource(R.string.all_set, name)
            else -> stringResource(R.string.you_owe, name)
        }
        val amountColor: Color = when {
            amount > 0 -> Color.Green
            amount == 0.0 -> Color.Gray
            else -> Color.Red
        }
        Text(
            text = nameBody,
            modifier = Modifier.padding(end = 12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Text(
            text = amount.absoluteValue.toString(),
            modifier = Modifier.padding(end = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = amountColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanionSelector(
    companionList: List<CompanionModel>,
    textFieldValue: String,
    requestCompanionResult: (companion: CompanionModel) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorText: @Composable () -> Unit = {}
) {
    var isExpanded: Boolean by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Open companion list"
                )
            },
            label = { Text(text = label) },
            isError = isError,
            supportingText = errorText
        )
        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            companionList.forEach { companion ->
                DropdownMenuItem(
                    text = { Text(companion.name) },
                    onClick = {
                        requestCompanionResult(companion)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun CreatePaymentButton(selectedTabIndex: Int, onClick: () -> Unit) {
    if (selectedTabIndex != indexOfDetailsTab()) return
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.create)
        )
    }
}

@Composable
private fun indexOfDetailsTab() = tabs.indexOf(R.string.details)

@Composable
private fun indexOfResultsTab() = tabs.indexOf(R.string.result)

@Composable
private fun PaymentsList(modifier: Modifier = Modifier, expenditures: List<ExpenditureModel>) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(expenditures) { expenditure ->
            PaymentItem(expenditure)
        }
    }
}

@Composable
@Preview
private fun PreviewDetails() {
    AppTheme {
        TripView(
            backNavigation = {},
            expenditures = listOf(
                ExpenditureModel(
                    uid = UUID.randomUUID().toString(),
                    payer = CompanionModel(
                        uid = UUID.randomUUID().toString(),
                        name = "Anton"
                    ),
                    debtors = listOf(
                        CompanionModel(
                            uid = UUID.randomUUID().toString(),
                            name = "Zea"
                        ),
                    ),
                    detail = "Test payment",
                    amountSpent = 10.0,
                    date = Date()
                ),
                ExpenditureModel(
                    uid = UUID.randomUUID().toString(),
                    payer = CompanionModel(
                        uid = UUID.randomUUID().toString(),
                        name = "Anton"
                    ),
                    debtors = listOf(
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
                    detail = "Test payment",
                    amountSpent = 10.0,
                    date = Date()
                ),
                ExpenditureModel(
                    uid = UUID.randomUUID().toString(),
                    payer = CompanionModel(
                        uid = UUID.randomUUID().toString(),
                        name = "Anton"
                    ),
                    debtors = listOf(
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
                        ),
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
                    detail = "Test payment",
                    amountSpent = 10.0,
                    date = Date()
                )
            ),
            createNewExpenditure = {},
            companionList = emptyList(),
            requestCompanionResult = {},
            topBarTitle = "Trip to Miami",
            resultModel = null
        )
    }
}

@Composable
@Preview
private fun PreviewResult() {
    AppTheme {
        TripView(
            backNavigation = {},
            expenditures = emptyList(),
            createNewExpenditure = {},
            topBarTitle = "Trip to Miami",
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
            requestCompanionResult = {},
            startingTab = indexOfResultsTab(),
            resultModel = ResultModel(
                companion = CompanionModel(
                    uid = UUID.randomUUID().toString(),
                    name = "Zea"
                ),
                totalPaid = 100.0,
                debts = mutableMapOf(
                    CompanionModel(uid = UUID.randomUUID().toString(), name = "Zea") to 12.0,
                    CompanionModel(uid = UUID.randomUUID().toString(), name = "Tongo") to 54.0,
                    CompanionModel(uid = UUID.randomUUID().toString(), name = "Jaz") to 42.0
                )
            )
        )
    }
}