package com.coronawarriors.verified.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coronawarriors.verified.enums.RequirementType
import java.util.*

@Entity(tableName = "requirement")
data class Requirements(
    @PrimaryKey(autoGenerate = true)
    private var id: Long,
    private var type: RequirementType,
    private var city: String = "",
    private var district: String = "",
    private var state: String = "",
    private var country: String = "",
    @ColumnInfo(name = "place_id") private var placeId: String = "",
    @ColumnInfo(name = "is_verified") private var isVerified: Boolean = false,
    @ColumnInfo(name = "available_quantity") private var availableQuantity: Long,
    @ColumnInfo(name = "last_verified") private var lastVerified: Date,
    private var contacts: List<Contact>
)
