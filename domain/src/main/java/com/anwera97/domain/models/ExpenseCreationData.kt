package com.anwera97.domain.models

import java.util.*

data class ExpenseCreationData(
    val id: Int = 0,
    val expense: Double,
    val date: Date,
    val tripId: Int,
    val payerId: Int,
    val detail: String?
)