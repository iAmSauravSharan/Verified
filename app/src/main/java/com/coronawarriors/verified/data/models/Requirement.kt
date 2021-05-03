package com.coronawarriors.verified.data.models

import android.os.Parcelable
import com.coronawarriors.verified.util.Constants.DEFAULT_COUNTRY
import com.coronawarriors.verified.util.Constants.EMPTY_STRING
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Requirement(
    val requirementType: String,
    val requirementNotes: String = EMPTY_STRING,
    val city: String,
    val state: String,
    val country: String = DEFAULT_COUNTRY,
) : Parcelable

@Parcelize
data class NewRequirement(
    val requirementType: String,
    val requirementNotes: String? = null,
    val city: String,
    val state: String,
    val country: String = DEFAULT_COUNTRY,
    val quantity: Int,
    val landmark: String? = null,
    var contactList: ArrayList<Contacts>?
) : Parcelable

@Parcelize
data class Contacts(
    val countryCode: String = "+91",
    val phoneNumber: String,
    val name: String? = null,
    val address: String? = null,
    val onlineAddress: String? = null,
    val isVerified: Boolean,
) : Parcelable

@Parcelize
data class RequirementDetail(
    @Expose @SerializedName("requirementType") val requirementType: String,
    @Expose @SerializedName("requirementNotes") val requirementNotes: String? = null,
    @Expose @SerializedName("city") val city: String,
    @Expose @SerializedName("state") val state: String,
    @Expose @SerializedName("country") val country: String = DEFAULT_COUNTRY,
    @Expose @SerializedName("quantity") val quantity: Int,
    @Expose @SerializedName("landmark") val landmark: String? = null,
    @Expose @SerializedName("contactList") var contactList: ArrayList<ContactDetail>?
) : Parcelable

@Parcelize
data class ContactDetail(
    @Expose @SerializedName("countryCode") val countryCode: String = "+91",
    @Expose @SerializedName("phoneNumber") val phoneNumber: String,
    @Expose @SerializedName("name") val name: String? = null,
    @Expose @SerializedName("address") val address: String? = null,
    @Expose @SerializedName("onlineAddress") val onlineAddress: String? = null,
    @Expose @SerializedName("isVerified") val isVerified: Boolean,
) : Parcelable
