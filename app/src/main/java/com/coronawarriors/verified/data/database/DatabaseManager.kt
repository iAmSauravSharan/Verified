package com.coronawarriors.verified.data.database

import com.coronawarriors.verified.data.database.entity.Contact
import com.coronawarriors.verified.data.database.entity.Requirements
import com.coronawarriors.verified.enums.RequirementType

interface DatabaseManager {

    suspend fun getRequirementByCity(city: String, isVerified: Boolean = false): List<Requirements>
    suspend fun getRequirementByPlaceId(placeId: String, isVerified: Boolean = false): List<Requirements>
    suspend fun getContactByRequirementType(placeId: String): List<Contact>
    suspend fun getContactByRequirementTypeAndCity(city: String, requirementType: RequirementType): List<Contact>
    suspend fun getVerifiedContactByRequirementTypeAndCity(city: String, requirementType: RequirementType, isVerified: Boolean = true): List<Contact>

}