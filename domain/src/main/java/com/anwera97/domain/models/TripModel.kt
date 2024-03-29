package com.anwera97.domain.models

class TripModel(
        val uid: String,
        val totalSpent: Double,
        val name: String,
        val companions: List<CompanionModel>
)