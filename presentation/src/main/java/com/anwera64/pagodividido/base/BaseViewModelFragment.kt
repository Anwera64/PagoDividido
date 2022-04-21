package com.anwera64.pagodividido.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseViewModelFragment<V : ViewModel, B : ViewDataBinding>(type: KClass<V>) : BaseFragment<B>() {

    protected val viewModel: V by currentScope.viewModel(this, type)
    abstract val viewModelValue: Int?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelValue?.let { value ->
            binding.setVariable(value, viewModel)
        }
        setupObservers()
    }

    abstract fun setupObservers()
}