package com.coronawarriors.verified.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.coronawarriors.verified.data.database.entity.Contact

@Dao
abstract class ContactDao : BaseDao<ContactDao> {

    /**
     * Get all data from the Data table.
     */
    @Query("SELECT * FROM contact")
    abstract suspend fun getContact(): List<Contact>
}