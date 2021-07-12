package com.anwera64.pagodividido.presentation.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.presentation.main.MainActivity
import com.anwera64.pagodividido.utils.StringUtils
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), LoginPresenter.LoginDelegate {

    var mPresenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email_sign_in_button.setOnClickListener { tryLogin() }

        if (mPresenter.doLogin()) {
            correctLogin()
        }
    }

    private fun tryLogin() {
        if (!checkForCompletion()) return
        mPresenter.doLogin(email.text.toString(), password.text.toString())
    }

    private fun checkForCompletion(): Boolean {
        var result: Boolean

        email.text.let {
            result = StringUtils.checkEmptyString(it)
        }

        password.text.let {
            result = StringUtils.checkEmptyString(it)
        }

        return result
    }

    override fun incorrectLogin(message: String) {
        Log.e("Login error:", message)
    }

    override fun correctLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
