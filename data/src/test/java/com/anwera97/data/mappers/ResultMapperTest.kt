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
        val aliceId = 1
        val bob = "Bob"
        val bobId = 2
        val charlie = "Charlie"
        val charlieId = 3

        val aliceTotalPaid = 20.0
        val aliceOwnShare = 10.0
        val bobOwesAlice = 5.0
        val charlieOwesAlice = aliceTotalPaid - aliceOwnShare - bobOwesAlice

        val bobTotalPaid = 30.0
        val bobOwnShare = 20.0
        val aliceOwesBob = bobTotalPaid - bobOwnShare
        
        val aliceCompanion = CompanionEntity(id = aliceId, name = alice, tripId = 100)
        val bobCompanion = CompanionEntity(id = bobId, name = bob, tripId = 100)

        val aggregationMap = mapOf(
            aliceCompanion to listOf(
                AggregatedDebt(aliceId, alice, aliceOwnShare),
                AggregatedDebt(bobId, bob, bobOwesAlice),
                AggregatedDebt(charlieId, charlie, charlieOwesAlice)
            ),
            bobCompanion to listOf(
                AggregatedDebt(bobId, bob, bobOwnShare),
                AggregatedDebt(aliceId, alice, aliceOwesBob)
            )
        )

        // When
        val results = ResultMapper.toModel(aggregationMap)

        // Then
        assertEquals(2, results.size)

        val aliceResult = results.find { it.companion.name == alice }!!
        assertEquals(aliceTotalPaid, aliceResult.totalPaid, 0.0)
        assertEquals(2, aliceResult.debts.size)
        
        val aliceDebts = aliceResult.debts.mapKeys { it.key.name }
        assertEquals(bobOwesAlice, aliceDebts[bob]!!, 0.0)
        assertEquals(charlieOwesAlice, aliceDebts[charlie]!!, 0.0)
        assertEquals(null, aliceDebts[alice])

        val bobResult = results.find { it.companion.name == bob }!!
        assertEquals(bobTotalPaid, bobResult.totalPaid, 0.0)
        assertEquals(1, bobResult.debts.size)
        
        val bobDebts = bobResult.debts.mapKeys { it.key.name }
        assertEquals(aliceOwesBob, bobDebts[alice]!!, 0.0)
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
        val aliceId = 1
        val aliceOwnShare = 15.0
        val aliceCompanion = CompanionEntity(id = aliceId, name = alice, tripId = 100)
        val aggregationMap = mapOf(
            aliceCompanion to listOf(AggregatedDebt(aliceId, alice, aliceOwnShare))
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
        val aliceId = 1
        val aliceCompanion = CompanionEntity(id = aliceId, name = alice, tripId = 100)
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

    @Test
    fun `given debts with floating point values when mapping to model then total is summed with precision`() {
        // Given
        val alice = "Alice"
        val aliceId = 1
        val bob = "Bob"
        val bobId = 2

        val aliceOwnShare = 0.1
        val bobDebtPart1 = 0.1
        val bobDebtPart2 = 0.1
        val expectedTotal = aliceOwnShare + bobDebtPart1 + bobDebtPart2

        val aliceCompanion = CompanionEntity(id = aliceId, name = alice, tripId = 100)
        val aggregationMap = mapOf(
            aliceCompanion to listOf(
                AggregatedDebt(aliceId, alice, aliceOwnShare),
                AggregatedDebt(bobId, bob, bobDebtPart1),
                AggregatedDebt(bobId, bob, bobDebtPart2)
            )
        )

        // When
        val results = ResultMapper.toModel(aggregationMap)

        // Then
        val aliceResult = results.first()
        assertEquals(expectedTotal, aliceResult.totalPaid, 0.000001)
    }

    @Test
    fun `given payer is not a debtor when mapping to model then totalPaid is correct and debts map contains all debtors`() {
        // Given
        val alice = "Alice"
        val aliceId = 1
        val bob = "Bob"
        val bobId = 2
        val charlie = "Charlie"
        val charlieId = 3

        val bobOwesAlice = 10.0
        val charlieOwesAlice = 5.0
        val expectedTotal = bobOwesAlice + charlieOwesAlice

        val aliceCompanion = CompanionEntity(id = aliceId, name = alice, tripId = 100)
        val aggregationMap = mapOf(
            aliceCompanion to listOf(
                AggregatedDebt(bobId, bob, bobOwesAlice),
                AggregatedDebt(charlieId, charlie, charlieOwesAlice)
            )
        )

        // When
        val results = ResultMapper.toModel(aggregationMap)

        // Then
        val aliceResult = results.first()
        assertEquals(expectedTotal, aliceResult.totalPaid, 0.0)
        assertEquals(2, aliceResult.debts.size)
        
        val aliceDebts = aliceResult.debts.mapKeys { it.key.name }
        assertEquals(bobOwesAlice, aliceDebts[bob]!!, 0.0)
        assertEquals(charlieOwesAlice, aliceDebts[charlie]!!, 0.0)
        assertTrue(!aliceDebts.containsKey(alice))
    }

    @Test
    fun `given debt with zero amount when mapping to model then it is included in debts map`() {
        // Given
        val alice = "Alice"
        val aliceId = 1
        val bob = "Bob"
        val bobId = 2
        val zeroDebt = 0.0
        val aliceOwnShare = 10.0
        val expectedTotal = aliceOwnShare + zeroDebt

        val aliceCompanion = CompanionEntity(id = aliceId, name = alice, tripId = 100)
        val aggregationMap = mapOf(
            aliceCompanion to listOf(
                AggregatedDebt(aliceId, alice, aliceOwnShare),
                AggregatedDebt(bobId, bob, zeroDebt)
            )
        )

        // When
        val results = ResultMapper.toModel(aggregationMap)

        // Then
        val aliceResult = results.first()
        assertEquals(expectedTotal, aliceResult.totalPaid, 0.0)
        
        val aliceDebts = aliceResult.debts.mapKeys { it.key.name }
        assertTrue(aliceDebts.containsKey(bob))
        assertEquals(zeroDebt, aliceDebts[bob]!!, 0.0)
    }
}
