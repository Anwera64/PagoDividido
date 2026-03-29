package com.anwera97.domain

import com.anwera97.domain.models.CompanionModel
import com.anwera97.domain.repositories.CompanionRepository
import com.anwera97.domain.usecases.CompanionsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CompanionsUseCaseTest {

    private val companionRepository: CompanionRepository = mock()
    private val companionsUseCase = CompanionsUseCase(companionRepository)

    @Test
    fun `get trip companions delegates to repository`() = runTest {
        val companions = listOf(
            CompanionModel(uid = "1", name = "Ana"),
            CompanionModel(uid = "2", name = "Bob")
        )
        whenever(companionRepository.getTripCompanions(7)) doReturn flowOf(companions)

        val result = companionsUseCase.getTripCompanions(7).first()

        assertEquals(2, result.size)
        assertEquals("Ana", result[0].name)
        assertEquals("Bob", result[1].name)
    }
}

