package com.anwera64.pagodividido.presentation

import android.app.Application
import com.anwera64.pagodividido.data.AppDatabase
import com.anwera64.pagodividido.domain.repository.TripRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PagoDividioApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TripRepository(database.tripDao()) }
}