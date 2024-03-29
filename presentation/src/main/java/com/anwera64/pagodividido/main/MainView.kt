package com.anwera64.pagodividido.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.base.AppTheme
import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.TripModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsContent(
    trips: List<TripModel>,
    createNewTripAction: () -> Unit,
    onTripSelected: (uid: String, name: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.title_trips))
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = createNewTripAction) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_new_trip)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        TripList(trips, onTripSelected, Modifier.padding(contentPadding))
    }
}


@Composable
private fun TripList(
    trips: List<TripModel>,
    onTripSelected: (uid: String, name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    LazyColumn(modifier = modifier.fillMaxSize(), state = scrollState) {
        items(trips) { tripModel ->
            TripItem(tripModel = tripModel, onTripSelected = onTripSelected)
        }
    }
}

@Composable
private fun TripItem(tripModel: TripModel, onTripSelected: (uid: String, name: String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onTripSelected(tripModel.uid, tripModel.name)
            }
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            /**
             * In compose setting the weight indicates that it should take all the space available
             * after the rest of the items are setup
             */
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = tripModel.name,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(id = R.string.amount_spent, tripModel.totalSpent),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = buildCompanionString(tripModel),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

private fun buildCompanionString(tripModel: TripModel): String {
    return tripModel.companions.joinToString { companion -> companion.name }
}

@Composable
@Preview
fun Test() {
    val trips = listOf(
        TripModel(
            UUID.randomUUID().toString(),
            12.0,
            "Test Trip",
            listOf(
                CompanionModel(UUID.randomUUID().toString(), "Anton"),
                CompanionModel(UUID.randomUUID().toString(), "Zea")
            )
        ),
        TripModel(
            UUID.randomUUID().toString(),
            12.0,
            "Test Trip 2",
            listOf(
                CompanionModel(UUID.randomUUID().toString(), "Anton"),
                CompanionModel(UUID.randomUUID().toString(), "Zea"),
                CompanionModel(UUID.randomUUID().toString(), "Bruce Wayne"),
                CompanionModel(UUID.randomUUID().toString(), "Carlos Juan de Dios Cruzado Vasquez")
            )
        )
    )
    AppTheme() {
        TripsContent(trips = trips, createNewTripAction = { }, onTripSelected = { _, _ -> })
    }
}