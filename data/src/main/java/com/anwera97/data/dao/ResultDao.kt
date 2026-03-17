package com.anwera97.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.anwera97.data.composedclasses.AggregatedDebt
import com.anwera97.data.entities.CompanionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {

    @Query("""
        SELECT 
            c_payer.*,
            c_debtor.id AS debtorId,
            c_debtor.name AS debtorName,
            SUM(d.amount) AS amount
        FROM companion AS c_payer
        INNER JOIN Expenditure AS e ON c_payer.id = e.payer_id
        INNER JOIN Debtors AS d ON e.id = d.expenditure_id
        INNER JOIN companion AS c_debtor ON d.companion_id = c_debtor.id
        WHERE c_payer.trip_id = :tripId
        GROUP BY c_payer.id, c_debtor.id
    """)
    fun getAggregatedDebts(tripId: Int): Flow<Map<CompanionEntity, List<AggregatedDebt>>>
}
