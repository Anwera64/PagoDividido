package com.anwera97.data.mappers

import com.anwera97.data.entities.CompanionEntity
import com.anwera97.domain.models.CompanionModel

object CompanionMapper {

    fun toModel(companionEntity: CompanionEntity): CompanionModel = with(companionEntity) {
        return CompanionModel(uid = id.toString(), name = name)
    }
}
