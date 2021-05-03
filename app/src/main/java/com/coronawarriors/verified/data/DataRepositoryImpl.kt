package com.coronawarriors.verified.data

import com.coronawarriors.verified.data.database.DatabaseManager
import com.coronawarriors.verified.data.database.entity.Contact
import com.coronawarriors.verified.data.database.entity.Requirements
import com.coronawarriors.verified.data.preference.PreferenceManager
import com.coronawarriors.verified.enums.RequirementType
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val database: DatabaseManager,
    private val preference: PreferenceManager
): DataRepository {

    override suspend fun getRequirementByCity(
        city: String,
        isVerified: Boolean
    ): List<Requirements> {
        return database.getRequirementByCity(city, isVerified)
    }

    override suspend fun getRequirementByPlaceId(
        placeId: String,
        isVerified: Boolean
    ): List<Requirements> {
        return database.getRequirementByPlaceId(placeId, isVerified)
    }

    override suspend fun getContactByRequirementType(placeId: String): List<Contact> {
        return database.getContactByRequirementType(placeId)
    }

    override suspend fun getContactByRequirementTypeAndCity(
        city: String,
        requirementType: RequirementType
    ): List<Contact> {
        return database.getContactByRequirementTypeAndCity(city, requirementType)
    }

    override suspend fun getVerifiedContactByRequirementTypeAndCity(
        city: String,
        requirementType: RequirementType,
        isVerified: Boolean
    ): List<Contact> {
        return database.getVerifiedContactByRequirementTypeAndCity(city, requirementType, isVerified)
    }

    override var isAppLaunchedForTheFirstTime: Boolean
        get() = preference.isAppLaunchedForTheFirstTime
        set(value) {preference.isAppLaunchedForTheFirstTime = value}
    override var isLoggedIn: Boolean
        get() = preference.isLoggedIn
        set(value) {preference.isLoggedIn = value}
    override var country: String?
        get() = preference.country
        set(value) {preference.country = value}
    override var state: String?
        get() = preference.state
        set(value) {preference.state = value}
    override var city: String?
        get() = preference.city
        set(value) {preference.city = value}
    override var userName: String?
        get() = preference.userName
        set(value) {preference.userName = value}
    override var userPic: String?
        get() = preference.userPic
        set(value) {preference.userPic = value}
    override var accountId: String?
        get() = preference.accountId
        set(value) {preference.accountId = value}
    override var accountIdToken: String?
        get() = preference.accountIdToken
        set(value) {preference.accountIdToken = value}
}