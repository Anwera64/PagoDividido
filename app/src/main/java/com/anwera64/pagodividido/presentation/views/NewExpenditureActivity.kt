package com.anwera64.pagodividido.presentation.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.presentation.presenters.NewExpenditurePresenter

class NewExpenditureActivity: AppCompatActivity(), NewExpenditurePresenter.NewExpenditureDelegate {

    private val mPresenter = NewExpenditurePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expenditure)
    }
}