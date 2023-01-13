package com.anwera64.pagodividido.base.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.anwera64.pagodividido.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BaseTopAppBar(backNavigation: () -> Unit, title: String) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = backNavigation) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        }
    )
}