package com.anwera97.domain.repositories

import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.models.ResultModel
import kotlinx.coroutines.flow.Flow

interface CompanionRepository {

    fun getTripCompanions(tripId: Int): Flow<List<CompanionModel>>

    fun getResultInfoFor(tripId: Int): Flow<List<ResultModel>>

    suspend fun insert(tripId: Int, vararg companionName: String)
}