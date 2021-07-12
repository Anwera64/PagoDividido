package com.anwera64.pagodividido.domain.models

import java.util.*

class TripModel(
    val uid: String,
    val totalSpent: Double,
    val name: String,
    val companions: List<CompanionModel>
)