package com.coronawarriors.verified.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id") private var id: Long,
    @ColumnInfo(name = "requirement_id") private var requirementId: Long,
    private var name: String,
    private var contact: String,
    private var country_code: String = "+91",
    private var city: String = "",
    private var district: String = "",
    private var state: String = "",
    private var country: String = "",
    private var address: String = "",
    private var onlineAddress: String = "",
    @ColumnInfo(name = "place_id") private var placeId: String = "",
    @ColumnInfo(name = "is_verified") private var isVerified: Boolean = false,
    @ColumnInfo(name = "is_government_official") private var isGovernmentOfficial: Boolean = false,
    @ColumnInfo(name = "last_contacted") private var lastContacted: Date,
)
