package com.coronawarriors.verified.data.database

import android.content.Context
import com.coronawarriors.verified.data.database.entity.Contact
import com.coronawarriors.verified.data.database.entity.Requirements
import com.coronawarriors.verified.enums.RequirementType
import dagger.hilt.android.qualifiers.ApplicationContext

class DatabaseManagerImpl(@ApplicationContext private val context: Context): DatabaseManager {

    private var database: AppDatabase = AppDatabase.getDatabase(context)
    override suspend fun getRequirementByCity(
        city: String,
        isVerified: Boolean
    ): List<Requirements> {
        return database.requirementDao().getRequirement()
    }

    override suspend fun getRequirementByPlaceId(
        placeId: String,
        isVerified: Boolean
    ): List<Requirements> {
        return database.requirementDao().getRequirement()
    }

    override suspend fun getContactByRequirementType(placeId: String): List<Contact> {
        return database.contactDao().getContact()
    }

    override suspend fun getContactByRequirementTypeAndCity(
        city: String,
        requirementType: RequirementType
    ): List<Contact> {
        return database.contactDao().getContact()
    }

    override suspend fun getVerifiedContactByRequirementTypeAndCity(
        city: String,
        requirementType: RequirementType,
        isVerified: Boolean
    ): List<Contact> {
        return database.contactDao().getContact()
    }


}