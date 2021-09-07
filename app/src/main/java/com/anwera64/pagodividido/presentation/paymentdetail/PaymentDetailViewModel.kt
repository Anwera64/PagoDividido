package com.anwera64.pagodividido.presentation.paymentdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.anwera64.pagodividido.domain.repository.ExpenditureRepository

class PaymentDetailViewModel(private val expenditureRepository: ExpenditureRepository) :
    ViewModel() {

    fun getPaymentDetail(id: String) = expenditureRepository.getDetail(id.toInt()).asLiveData()
}