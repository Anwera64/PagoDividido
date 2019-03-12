package com.anwera64.pagodividido.domain.models

import java.util.*

class Expenditure(val uid: String, val owner: Companion, val debtors: ArrayList<Companion>, val detail: String,
                  val amountSpent: Float, val date: Date)