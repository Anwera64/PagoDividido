package com.anwera64.pagodividido.newexpenditure.utils

class NewExpenseErrorModel(
    val totalAmountError: NewExpenseErrorStates?,
    val payerError: NewExpenseErrorStates?,
    val paymentWayError: NewExpenseErrorStates?,
    val paymentRelationshipErrors: Map<Int, NewExpenseErrorStates>
)
