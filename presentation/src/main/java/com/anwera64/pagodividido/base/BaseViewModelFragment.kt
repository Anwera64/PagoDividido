package com.anwera64.pagodividido.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import javax.inject.Inject

abstract class BaseViewModelFragment<V : ViewModel, B : ViewDataBinding> : BaseFragment<B>() {

    abstract val viewModel: V
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