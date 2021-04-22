package com.coronawarriors.verified.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.coronawarriors.verified.data.database.entity.Requirements

@Dao
abstract class RequirementsDao : BaseDao<Requirements> {

    /**
     * Get all data from the Data table.
     */
    @Query("SELECT * FROM requirement")
    abstract suspend fun getRequirement(): List<Requirements>
}