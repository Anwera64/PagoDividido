package com.anwera64.pagodividido

import android.app.Application

class PagoDividioApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

}