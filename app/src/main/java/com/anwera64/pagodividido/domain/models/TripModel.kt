package com.anwera64.pagodividido.domain.models

import java.util.*

class TripModel(val uid: String, val totalSpent: Float, val name: String,
                val companions: HashMap<String, Companion>, var expenditures: ArrayList<Expenditure>)