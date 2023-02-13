package com.anwera64.pagodividido.newexpenditure.utils

import androidx.annotation.StringRes
import com.anwera64.pagodividido.R
import com.anwera97.domain.models.DebtorInputErrorReasons
import com.anwera97.domain.models.InputErrorType


enum class NewExpenseErrorStates(@StringRes val errorResource: Int) {
    NOT_A_NUMBER(R.string.error_non_numeral_amount),
    NO_INPUT(R.string.error_empty_amount),
    REQUIRED_FIELD(R.string.error_field_required),
    ZERO_OR_NEGATIVE(R.string.error_debtor_amount_0),
    OVER_MAX_AMOUNT(R.string.error_debtor_amount_over_max),
    SUM_EXCEEDS_MAX_AMOUNT(R.string.error_debtor_amount_over_max);

    companion object {
        fun InputErrorType.toErrorState(): NewExpenseErrorStates? {
            return when (this) {
                InputErrorType.INPUT_NUMBER -> NOT_A_NUMBER
                InputErrorType.INPUT_AMOUNT -> NO_INPUT
                InputErrorType.MISSING_FIELD -> REQUIRED_FIELD
                else -> null
            }
        }

        fun DebtorInputErrorReasons.toErrorState(): NewExpenseErrorStates {
            return when (this) {
                DebtorInputErrorReasons.ZERO_OR_NEGATIVE -> ZERO_OR_NEGATIVE
                DebtorInputErrorReasons.OVER_MAX_AMOUNT -> OVER_MAX_AMOUNT
                DebtorInputErrorReasons.SUM_EXCEEDS_MAX_AMOUNT -> SUM_EXCEEDS_MAX_AMOUNT
            }
        }
    }
}