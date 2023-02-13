package com.anwera64.pagodividido.base

import android.os.Bundle
import androidx.lifecycle.ViewModel

abstract class BaseComposeViewModelActivity<V : ViewModel> :
    BaseComposeActivity() {

    abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    abstract fun setupObservers()
}
