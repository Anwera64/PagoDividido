package com.anwera64.pagodividido.presentation.presenters

import com.anwera64.pagodividido.domain.FirebaseAdapter

class NewExpenditurePresenter(view: NewExpenditureDelegate) {

    val dbRef = FirebaseAdapter.instance.db.reference
    val userID = FirebaseAdapter.instance.mAuth.currentUser!!.uid



    interface NewExpenditureDelegate {

    }
}