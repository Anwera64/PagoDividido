package com.anwera97.domain.models

data class DebtorInputError(val companionId: Int, val reason: DebtorInputErrorReasons)

enum class DebtorInputErrorReasons {
    ZERO_OR_NEGATIVE, OVER_MAX_AMOUNT, SUM_EXCEEDS_MAX_AMOUNT
}

