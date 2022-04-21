package com.anwera64.pagodividido.utils

import com.anwera97.domain.models.CompanionModel

object CompanionsUtil {

    @JvmStatic
    fun companionsToStrings(companions: List<CompanionModel>): String {
        var result = ""
        companions.forEachIndexed { index, companion ->
            val separator = if (index < companions.size - 1) ", " else ""
            result += "${companion.name}$separator"
        }
        return result.trim()
    }
}