package com.anwera64.pagodividido.newexpenditure.utils

data class NewExpenseFormData(
    val amount: String? = null,
    val payerId: String? = null,
    val detail: String? = null,
    val paymentOption: PaymentOptions? = null,
    val payingRelationship: Map<Int, Double> = mutableMapOf()
) : java.io.Serializable