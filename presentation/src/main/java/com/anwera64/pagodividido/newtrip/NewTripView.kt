package com.anwera64.pagodividido.newtrip

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anwera64.pagodividido.R
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTripContent(
    companions: List<String>,
    onDeleteCompanion: (name: String) -> Unit,
    onCreateTrip: (tripName: String, companions: List<String>) -> Unit,
    onCreateCompanion: (name: String) -> Unit,
    errors: Set<ErrorStates> = emptySet(),
    backNavigation: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.title_new_trip))
                },
                navigationIcon = {
                    IconButton(onClick = backNavigation) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onCreateTrip(title, companions) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.create)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        Form(
            modifier = Modifier.padding(contentPadding),
            companions = companions,
            onDeleteCompanion,
            tripTitle = title,
            onTripTitleValueChange = { newTitle -> title = newTitle },
            errors = errors,
            onCreateCompanion = onCreateCompanion
        )
    }
}

@Composable
private fun Form(
    modifier: Modifier = Modifier,
    companions: List<String>,
    onDeleteCompanion: (name: String) -> Unit,
    tripTitle: String,
    onTripTitleValueChange: (String) -> Unit,
    errors: Set<ErrorStates>,
    onCreateCompanion: (name: String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        TitleField(
            modifier = Modifier.padding(top = 24.dp),
            title = tripTitle,
            onValueChange = onTripTitleValueChange,
            errors = errors
        )
        CompanionField(
            modifier = Modifier.padding(top = 16.dp),
            errors = errors,
            onCreateCompanion = onCreateCompanion
        )
        CompanionChips(
            companions = companions,
            onDeleteCompanion = onDeleteCompanion,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CompanionField(
    modifier: Modifier = Modifier,
    errors: Set<ErrorStates>,
    onCreateCompanion: (name: String) -> Unit
) {
    var isError: Boolean = errors.contains(ErrorStates.NOT_ENOUGH_COMPANIONS)
    var companionName: String by remember { mutableStateOf("") }
    TextField(
        value = companionName,
        onValueChange = { newValue ->
            companionName = newValue
            if (companionName.isNotEmpty()) {
                isError = false
            }
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(id = R.string.companion_name)) },
        trailingIcon = {
            if (companionName.isNotEmpty()) {
                IconButton(
                    onClick = {
                        // Add chip
                        onCreateCompanion(companionName)
                        companionName = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        stringResource(id = R.string.add_companion)
                    )
                }
            }
        },
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = ErrorStates.NOT_ENOUGH_COMPANIONS.errorResource),
                    textAlign = TextAlign.End,
                )
            }
        },
        isError = isError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanionChips(
    companions: List<String>,
    onDeleteCompanion: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        mainAxisSpacing = 8.dp
    ) {
        companions.forEach { companion ->
            AssistChip(
                onClick = { },
                trailingIcon = {
                    IconButton(
                        onClick = { onDeleteCompanion(companion) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.delete_companion),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                label = { Text(text = companion) }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TitleField(
    modifier: Modifier = Modifier,
    title: String,
    onValueChange: (String) -> Unit,
    errors: Set<ErrorStates>,
) {
    val isError = errors.contains(ErrorStates.EMPTY_TITLE)
    TextField(
        value = title,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(id = R.string.trip_name)) },
        isError = isError,
        supportingText = {
            if (isError) Text(text = stringResource(id = ErrorStates.EMPTY_TITLE.errorResource))
        }
    )
}

@Composable
@Preview
fun Preview() {
    NewTripContent(
        companions = listOf(
            "Zea",
            "Anton",
            "Jazmin",
            "Tongo",
            "Carlos Juan de Dios Cruzado Vazquez"
        ),
        onDeleteCompanion = {},
        onCreateTrip = { _, _ -> },
        onCreateCompanion = {},
        backNavigation = {}
    )
}