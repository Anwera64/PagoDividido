package com.anwera64.pagodividido.presentation.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.presentation.presenters.NewExpenditurePresenter

class NewExpenditureActivity: AppCompatActivity(), NewExpenditurePresenter.NewExpenditureDelegate {

    private var mPresenter: NewExpenditurePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expenditure)

        if (intent.hasExtra("tripUid")) {
            val tripUid = intent.getStringExtra("tripUid")
            mPresenter = NewExpenditurePresenter(this, tripUid)
        }
    }

    private fun createCompanionRadioButton() {
        //TODO
    }

    private fun createExpenditure() {
        //mPresenter.createExpenditure()
    }

    override fun onExpenditureCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}