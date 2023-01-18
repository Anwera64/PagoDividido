package com.anwera64.pagodividido.trip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.base.AppTheme
import com.anwera64.pagodividido.base.compose.BaseTopAppBar
import com.anwera64.pagodividido.utils.DateFormatter
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.domain.models.ResultModel
import java.util.*
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
            var textFieldValue: String by remember { mutableStateOf(selectorHint) }
            if (resultModel !== null) textFieldValue = resultModel.companion.name
            CompanionSelector(
                companionList = companionList,
                textFieldValue = textFieldValue,
                requestCompanionResult = { companion ->
                    requestCompanionResult(companion.uid)
                    textFieldValue = companion.name
                }
            )
            TotalSpentText(resultModel)
        }
        items(resultModel?.debts?.toList().orEmpty()) { (name, amount) ->
            DebtItem(name, amount)
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
private fun CompanionSelector(
    companionList: List<CompanionModel>,
    textFieldValue: String,
    requestCompanionResult: (companion: CompanionModel) -> Unit
) {
    var isExpanded: Boolean by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Open companion list"
                )
            }
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
@OptIn(ExperimentalMaterial3Api::class)
private fun PaymentItem(expenditure: ExpenditureModel) {
    ElevatedCard(
        onClick = { /*TODO This should open the expense detail*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
        ) {
            DateText(expenditure = expenditure)
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                DetailText(expenditure = expenditure)
                PayerText(expenditure = expenditure)
            }
            Spacer(Modifier.weight(1f))
            AmountText(expenditure = expenditure)
        }
    }
}

@Composable
private fun DetailText(expenditure: ExpenditureModel) {
    val detailString = expenditure.detail ?: stringResource(R.string.no_detail)
    Text(
        text = stringResource(R.string.detail, detailString),
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
private fun PayerText(expenditure: ExpenditureModel) {
    Text(
        stringResource(R.string.who_paid, expenditure.payer.name),
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun AmountText(expenditure: ExpenditureModel) {
    Text(
        text = expenditure.amountSpent.toString(),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun DateText(expenditure: ExpenditureModel) {
    Text(
        text = DateFormatter.formatDate(expenditure.date),
        style = MaterialTheme.typography.labelLarge
    )
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
                    "Zea" to 12.0,
                    "Tongo" to 54.0,
                    "Jaz" to 42.0
                )
            )
        )
    }
}