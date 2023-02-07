package com.anwera64.pagodividido.expensedetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.base.compose.AppTheme
import com.anwera64.pagodividido.base.compose.BaseTopAppBar
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.DebtorModel
import com.anwera97.domain.models.ExpenditureDetailModel
import com.anwera97.domain.models.ExpenditureModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailContent(
    expenseModel: ExpenditureDetailModel? = null,
    backNavigation: () -> Unit
) {
    Scaffold(
        topBar = {
            BaseTopAppBar(
                backNavigation = backNavigation,
                title = stringResource(id = R.string.details)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        if (expenseModel == null) {
            EmptyView(contentPadding)
        } else {
            ExpenseDetails(expenseModel = expenseModel, modifier = Modifier.padding(contentPadding))
        }
    }
}

@Composable
private fun EmptyView(contentPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
    ) {
        Text(
            text = "No expense details found",
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ExpenseDetails(expenseModel: ExpenditureDetailModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text(
                text = expenseModel.detail ?: stringResource(id = R.string.no_detail),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(
                    id = R.string.amount_spent_placeholder,
                    expenseModel.amountSpent
                ),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(id = R.string.payer_placeholder, expenseModel.payer.name),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Expense details",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        items(expenseModel.debtors) { debtor ->
            val annotatedText = buildDebtorString(debtor, expenseModel.payer.uid.toInt())
            Text(
                text = annotatedText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp, start = 8.dp)
            )
        }
    }

}

@Composable
private fun buildDebtorString(debtor: DebtorModel, payerId: Int): AnnotatedString {
    val annotatedText = buildAnnotatedString {
        val baseString = stringResource(
            id = R.string.companion_benefited_placeholder,
            debtor.name,
            debtor.amount
        )
        append(baseString)
        val amountString = debtor.amount.toString()
        val color = if (debtor.id != payerId) (Color.Red) else Color.Green
        addStyle(
            style = SpanStyle(color),
            start = baseString.length - amountString.length - 1,
            end = baseString.length
        )
    }
    return annotatedText
}

@Composable
@Preview
fun Preview() {
    AppTheme() {
        ExpenseDetailContent(
            backNavigation = {},
            expenseModel = ExpenditureDetailModel(
                uid = UUID.randomUUID().toString(),
                payer = CompanionModel(
                    uid = UUID.randomUUID().toString(),
                    name = "Anton"
                ),
                detail = "Pago de prueba",
                amountSpent = 100.0,
                date = Date(),
                debtors = listOf(
                    DebtorModel(
                        id = 1,
                        amount = 50.0,
                        name = "Anton"
                    ),
                    DebtorModel(
                        id = 2,
                        amount = 50.0,
                        name = "Zea"
                    )
                )
            )
        )
    }
}