package com.anwera64.pagodividido.presentation

import android.app.Application
import com.anwera64.pagodividido.data.AppDatabase
import com.anwera64.pagodividido.domain.repository.CompanionRepository
import com.anwera64.pagodividido.domain.repository.TripRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PagoDividioApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val tripRepository by lazy { TripRepository(database.tripDao()) }
    val companionRepository by lazy { CompanionRepository(database.companionDao()) }
}