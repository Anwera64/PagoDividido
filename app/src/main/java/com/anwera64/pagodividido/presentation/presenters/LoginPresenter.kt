package com.anwera64.pagodividido.presentation.presenters

import com.anwera64.pagodividido.domain.FirebaseAdapter

class LoginPresenter(private val view: LoginDelegate) {

    private val TAG = "LoginPresenter"
    private var mAuth = FirebaseAdapter.instance.mAuth

    fun doLogin(): Boolean {
        mAuth.currentUser?.let {
            return true
        }

        return false
    }

    fun doLogin(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    view.correctLogin()
                }
                .addOnFailureListener { exception -> view.incorrectLogin(exception.localizedMessage) }
    }

    interface LoginDelegate {

        fun incorrectLogin(message: String)

        fun correctLogin()
    }
}