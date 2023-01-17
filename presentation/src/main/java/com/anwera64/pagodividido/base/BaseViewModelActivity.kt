package com.anwera64.pagodividido.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import javax.inject.Inject

abstract class BaseViewModelActivity<V : ViewModel, B : ViewDataBinding> : BaseActivity<B>() {

    abstract val viewModel: V
    abstract val viewModelValue: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelValue?.let { value ->
            binding.setVariable(value, viewModel)
        }
        setupObservers()
    }

    abstract fun setupObservers()
}

