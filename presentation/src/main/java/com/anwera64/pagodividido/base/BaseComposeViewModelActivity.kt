package com.anwera64.pagodividido.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseComposeViewModelActivity<V : ViewModel>(type: KClass<V>) :
    BaseComposeActivity() {

    protected val viewModel: V by currentScope.viewModel(this, type)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    abstract fun setupObservers()
}
