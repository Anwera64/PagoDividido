package com.anwera97.pagodividido.trip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anwera97.domain.models.ExpenditureModel
import com.anwera97.pagodividido.R
import com.anwera97.pagodividido.utils.DateFormatter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PaymentItem(expenditure: ExpenditureModel) {
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
        text = stringResource(R.string.detail_placeholder, detailString),
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
private fun PayerText(expenditure: ExpenditureModel) {
    Text(
        stringResource(R.string.who_paid_placeholder, expenditure.payer.name),
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