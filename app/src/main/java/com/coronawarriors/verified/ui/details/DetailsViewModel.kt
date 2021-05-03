package com.coronawarriors.verified.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coronawarriors.verified.data.models.Requirement
import com.coronawarriors.verified.data.models.RequirementDetail
import com.coronawarriors.verified.enums.DetailsType
import com.coronawarriors.verified.util.Constants.REQUIREMENTS_CHILD
import com.google.firebase.database.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {

    private lateinit var detailsType: DetailsType
    private lateinit var requiremnt: Requirement

    private val _searchResult = MutableLiveData<ArrayList<RequirementDetail>>()
    val searchResult: LiveData<ArrayList<RequirementDetail>>
        get() = _searchResult

    //    private var firebaseDatabase: FirebaseDatabase = VerifiedApp.coronaWarriorsDB
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var detailsDb: DatabaseReference =
        firebaseDatabase.reference.child(REQUIREMENTS_CHILD)

    fun setDetailsType(detailsType: DetailsType) {
        this.detailsType = detailsType
    }

    fun getDetailsType() = detailsType

    fun setRequirement(requirement: Requirement) {
        this.requiremnt = requirement
    }

    fun getRequirement() = requiremnt

    fun fetchDetails() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val resultList = ArrayList<RequirementDetail>()
                for (countryArray in dataSnapshot.children) {
                    if (countryArray.key.equals(requiremnt.country, ignoreCase = true)) {
                        for (stateArray in countryArray.children) {
                            if (stateArray.key.equals(requiremnt.state, ignoreCase = true)) {
                                for (cityArray in stateArray.children) {
                                    if (cityArray.key.equals(requiremnt.city, ignoreCase = true)) {
                                        for (dataArray in cityArray.children) {
                                            try {
                                                val data: String = dataArray.value.toString()
                                                val requirement: RequirementDetail =
                                                    Gson().fromJson(
                                                        data,
                                                        RequirementDetail::class.java
                                                    )
                                                resultList.add(requirement)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Log.d(
                        "firebase",
                        countryArray.key + ": " + countryArray.value.toString()
                    )
                }
                _searchResult.value = resultList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        detailsDb.addValueEventListener(postListener)
    }

}