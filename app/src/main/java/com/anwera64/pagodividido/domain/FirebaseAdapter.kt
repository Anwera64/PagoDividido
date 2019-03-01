package com.anwera64.pagodividido.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseAdapter {

    val db = FirebaseDatabase.getInstance()
    val mAuth = FirebaseAuth.getInstance()

    init {
        db.setPersistenceEnabled(true)
    }

    companion object {
        val instance = FirebaseAdapter()
    }

}