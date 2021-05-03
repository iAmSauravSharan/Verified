package com.coronawarriors.verified.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coronawarriors.verified.data.DataRepository
import com.coronawarriors.verified.enums.SearchUIListener
import com.coronawarriors.verified.enums.SearchUIListener.ON_BACK
import com.coronawarriors.verified.enums.SearchUIListener.ON_CLEAR
import com.coronawarriors.verified.util.Constants.CITY
import com.coronawarriors.verified.util.Constants.COUNTRY
import com.coronawarriors.verified.util.Constants.EMPTY_STRING
import com.coronawarriors.verified.util.Constants.PLACES_CHILD
import com.coronawarriors.verified.util.Constants.REQUIREMENT_TYPE_CHILD
import com.coronawarriors.verified.util.Constants.STATE
import com.coronawarriors.verified.util.Constants.TITLE
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _clickListener = MutableLiveData<SearchUIListener>()
    val clickListener: LiveData<SearchUIListener>
        get() = _clickListener

    private val _searchResult = MutableLiveData<ArrayList<String>>()
    val searchResult: LiveData<ArrayList<String>>
        get() = _searchResult

    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var placesDb: DatabaseReference = firebaseDatabase.reference.child(PLACES_CHILD)
    private var requirementTypeDb: DatabaseReference =
        firebaseDatabase.reference.child(REQUIREMENT_TYPE_CHILD)

    private var searchType: String = EMPTY_STRING

    fun getSearchType(): String = searchType

    fun setSearchType(searchType: String) {
        this.searchType = searchType
    }


    public fun onBackButtonClicked() {
        _clickListener.value = ON_BACK
    }

    public fun onClearTextClicked() {
        _clickListener.value = ON_CLEAR
    }

    fun fetchPlacesResult() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val resultList = ArrayList<String>()
                for (childSnapshot in dataSnapshot.children) {
                    when (searchType) {
                        COUNTRY -> {
                            resultList.add(childSnapshot.key.toString())
                        }
                        STATE -> {
                            if (childSnapshot.key.toString()
                                    .equals(repository.country, ignoreCase = true)
                            ) {
                                for (stateSnapShot in childSnapshot.children) {
                                    val state = stateSnapShot.children
                                    for (stateName in state) resultList.add(stateName.key.toString())
                                }
                            }
                        }
                        CITY -> {
                            if (childSnapshot.key.toString()
                                    .equals(repository.country, ignoreCase = true)
                            ) {
                                for (stateSnapShot in childSnapshot.children) {
                                    val state = stateSnapShot.children
                                    for (stateName in state)
                                        if (stateName.key.toString()
                                                .equals(repository.state, ignoreCase = true)
                                        ) {
                                            for (citySnapShot in stateName.children) {
                                                resultList.add(citySnapShot.value.toString())
                                            }
                                        }
                                }
                            }
                        }
                    }
                    _searchResult.value = resultList
                    Log.d(
                        "firebase",
                        childSnapshot.key + ": " + childSnapshot.value.toString()
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        placesDb.addValueEventListener(postListener)
    }

    fun fetchRequirementTypes() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val resultList = ArrayList<String>()
                for (childSnapshot in dataSnapshot.children) {
                    for (requirementSnapShot in childSnapshot.children) {
                        if (requirementSnapShot.key == (TITLE))
                            resultList.add(requirementSnapShot.value.toString())
                    }

                    Log.d(
                        "firebase",
                        childSnapshot.key + ": " + childSnapshot.value.toString()
                    )
                }
                _searchResult.value = resultList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        requirementTypeDb.addValueEventListener(postListener)
    }
}