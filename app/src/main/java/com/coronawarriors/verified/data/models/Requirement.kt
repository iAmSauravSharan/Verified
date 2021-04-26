package com.coronawarriors.verified.data.models

import android.os.Parcelable
import com.coronawarriors.verified.util.Constants.DEFAULT_COUNTRY
import com.coronawarriors.verified.util.Constants.EMPTY_STRING
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Requirement(
    private val requirementType: String,
    private val requirementNotes: String = EMPTY_STRING,
    private val city: String,
    private val state: String,
    private val country: String = DEFAULT_COUNTRY,
) : Parcelable

@Parcelize
data class NewRequirement(
    private val requirementType: String,
    private val requirementNotes: String = EMPTY_STRING,
    private val city: String,
    private val state: String,
    private val country: String = DEFAULT_COUNTRY,
    private val quantity: Int,
    private val landmark: String,
    private val contactList: ArrayList<Contacts>
) : Parcelable

@Parcelize
data class Contacts(
    private val countryCode: String = "+91",
    private val phoneNumber: String,
    private val name: String,
    private val address: String,
    private val onlineAddress: String,
    private val isVerified: Boolean,
) : Parcelable
