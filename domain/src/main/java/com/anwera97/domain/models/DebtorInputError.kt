package com.anwera97.domain.models

class DebtorInputError(val viewTag: Int, val reason: DebtorInputErrorReasons)

enum class DebtorInputErrorReasons {
    ZERO_OR_NEGATIVE, OVER_MAX_AMOUNT, SUM_EXCEEDS_MAX_AMOUNT
}

