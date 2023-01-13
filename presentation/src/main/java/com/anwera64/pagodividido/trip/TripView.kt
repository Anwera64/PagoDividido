package com.anwera64.pagodividido.trip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.util.*

private val tabs: List<Int> = listOf(R.string.details, R.string.result)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripView(
    topBarTitle: String,
    backNavigation: () -> Unit,
    expenditures: List<ExpenditureModel>,
    createNewExpenditure: () -> Unit,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
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
        if (selectedTabIndex == indexOfDetailsTab()) {
            PaymentsList(
                modifier = Modifier.padding(contentPadding),
                expenditures = expenditures
            )
        } else {
            //TODO()
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
    Text(
        text = stringResource(id = R.string.detail) + " ${expenditure.detail}",
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
private fun PayerText(expenditure: ExpenditureModel) {
    Text(
        stringResource(id = R.string.who_payed) + " ${expenditure.payer.name}",
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
private fun Preview() {
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
            topBarTitle = "Trip to Miami"
        )
    }
}