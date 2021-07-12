package com.anwera64.pagodividido.presentation

import android.app.Application
import com.anwera64.pagodividido.data.AppDatabase
import com.anwera64.pagodividido.domain.repository.TripRepository

class PagoDividioApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { TripRepository(database.tripDao()) }
}