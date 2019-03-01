package com.anwera64.pagodividido.domain.models

import java.util.*

class Trip(val uid: String, val totalSpent: Float,
           val companions: HashMap<String, Companion>, var payments: ArrayList<Expenditure>)