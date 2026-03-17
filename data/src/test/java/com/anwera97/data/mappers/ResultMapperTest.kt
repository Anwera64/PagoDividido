package com.anwera97.data.mappers

import com.anwera97.data.composedclasses.AggregatedDebt
import com.anwera97.data.entities.CompanionEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultMapperTest {

    @Test
    fun `given aggregation map when mapping to model then returns list of ResultModels correctly`() {
        // Given
        val alice = "Alice"
        val bob = "Bob"
        val charlie = "Charlie"

        val aliceTotalPaid = 20.0
        val aliceOwnShare = 10.0
        val bobOwesAlice = 5.0
        val charlieOwesAlice = aliceTotalPaid - aliceOwnShare - bobOwesAlice

        val bobTotalPaid = 30.0
        val bobOwnShare = 20.0
        val aliceOwesBob = bobTotalPaid - bobOwnShare
        
        val aliceCompanion = CompanionEntity(id = 1, name = alice, tripId = 100)
        val bobCompanion = CompanionEntity(id = 2, name = bob, tripId = 100)

        val aggregationMap = mapOf(
            aliceCompanion to listOf(
                AggregatedDebt(alice, aliceOwnShare),
                AggregatedDebt(bob, bobOwesAlice),
                AggregatedDebt(charlie, charlieOwesAlice)
            ),
            bobCompanion to listOf(
                AggregatedDebt(bob, bobOwnShare),
                AggregatedDebt(alice, aliceOwesBob)
            )
        )

        // When
        val results = ResultMapper.toModel(aggregationMap)

        // Then
        assertEquals(2, results.size)

        val aliceResult = results.find { it.companion.name == alice }!!
        assertEquals(aliceTotalPaid, aliceResult.totalPaid, 0.0)
        assertEquals(2, aliceResult.debts.size)
        assertEquals(bobOwesAlice, aliceResult.debts[bob]!!, 0.0)
        assertEquals(charlieOwesAlice, aliceResult.debts[charlie]!!, 0.0)
        assertEquals(null, aliceResult.debts[alice])

        val bobResult = results.find { it.companion.name == bob }!!
        assertEquals(bobTotalPaid, bobResult.totalPaid, 0.0)
        assertEquals(1, bobResult.debts.size)
        assertEquals(aliceOwesBob, bobResult.debts[alice]!!, 0.0)
    }

    @Test
    fun `given empty aggregation map when mapping to model then returns empty list`() {
        // Given
        val emptyMap = emptyMap<CompanionEntity, List<AggregatedDebt>>()

        // When
        val results = ResultMapper.toModel(emptyMap)

        // Then
        assertTrue(results.isEmpty())
    }

    @Test
    fun `given payer with only own debt when mapping to model then debts map is empty`() {
        // Given
        val alice = "Alice"
        val aliceOwnShare = 15.0
        val aliceCompanion = CompanionEntity(id = 1, name = alice, tripId = 100)
        val aggregationMap = mapOf(
            aliceCompanion to listOf(AggregatedDebt(alice, aliceOwnShare))
        )

        // When
        val results = ResultMapper.toModel(aggregationMap)

        // Then
        assertEquals(1, results.size)
        val aliceResult = results.first()
        assertEquals(aliceOwnShare, aliceResult.totalPaid, 0.0)
        assertTrue(aliceResult.debts.isEmpty())
    }

    @Test
    fun `given payer with no debts at all when mapping to model then totalPaid is zero and debts map is empty`() {
        // Given
        val alice = "Alice"
        val aliceCompanion = CompanionEntity(id = 1, name = alice, tripId = 100)
        val aggregationMap = mapOf(
            aliceCompanion to emptyList<AggregatedDebt>()
        )

        // When
        val results = ResultMapper.toModel(aggregationMap)

        // Then
        assertEquals(1, results.size)
        val aliceResult = results.first()
        assertEquals(0.0, aliceResult.totalPaid, 0.0)
        assertTrue(aliceResult.debts.isEmpty())
    }
}
