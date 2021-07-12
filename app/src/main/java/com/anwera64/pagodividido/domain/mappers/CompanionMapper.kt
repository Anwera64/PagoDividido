package com.anwera64.pagodividido.domain.mappers

import com.anwera64.pagodividido.data.entities.Companion
import com.anwera64.pagodividido.domain.models.CompanionModel

object CompanionMapper {

    fun toModel(companionEntity: Companion): CompanionModel = with(companionEntity) {
        return CompanionModel(uid = id.toString(), name = name)
    }
}