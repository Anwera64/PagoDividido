package com.anwera64.pagodividido.presentation

import android.app.Application

class PagoDividioApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

}