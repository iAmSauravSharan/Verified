package com.coronawarriors.verified.ui.form

import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coronawarriors.verified.data.DataRepository
import com.coronawarriors.verified.data.database.entity.Requirements
import com.coronawarriors.verified.data.models.Contacts
import com.coronawarriors.verified.data.models.NewRequirement
import com.coronawarriors.verified.data.models.Requirement
import com.coronawarriors.verified.enums.FormError
import com.coronawarriors.verified.enums.FormType
import com.coronawarriors.verified.enums.FormType.SEARCH_FORM
import com.coronawarriors.verified.enums.FormType.UPDATE_FORM
import com.coronawarriors.verified.enums.FormUIClickListeners
import com.coronawarriors.verified.enums.FormUIClickListeners.ON_FAILURE
import com.coronawarriors.verified.enums.FormUIClickListeners.ON_SUCCESS
import com.coronawarriors.verified.util.Constants.DEFAULT_COUNTRY
import com.coronawarriors.verified.util.Constants.EMPTY_STRING
import com.coronawarriors.verified.util.Constants.REQUIREMENTS_CHILD
import com.coronawarriors.verified.util.Constants.VALUE_ZERO
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private var formType = SEARCH_FORM
    private var isUploaded: Boolean = false

    private val _clickListener = MutableLiveData<FormUIClickListeners>()
    val clickListener: LiveData<FormUIClickListeners>
        get() = _clickListener

    private val _formError = MutableLiveData<FormError>()
    val formError: LiveData<FormError>
        get() = _formError

    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var requirementDB: DatabaseReference =
        firebaseDatabase.reference.child(REQUIREMENTS_CHILD)

    public val requirementType: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val extraNotes: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val state: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val city: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val landMark: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val quantity: ObservableField<String> = ObservableField(EMPTY_STRING)

    private lateinit var requirements: Requirement
    private lateinit var newRequirements: NewRequirement
    private var lastRequirements: NewRequirement? = null
    var contactList: ArrayList<Contacts> = java.util.ArrayList()

    init {

    }

    fun setRequirement(requirements: Requirement) {
        this.requirements = requirements
    }

    fun getRequirement(): Requirement = this.requirements

    fun setNewRequirement(requirement: NewRequirement) {
        this.newRequirements = requirement
    }

    fun getNewRequirement(): NewRequirement = this.newRequirements

    fun setFormType(type: FormType) {
        this.formType = type
    }

    fun getFormType(): FormType = this.formType

    public fun onSubmit() {
        if (!isValidForm(formType)) return
        if (formType == SEARCH_FORM) {
            requirements = Requirement(
                requirementType.get().toString(),
                requirementNotes = extraNotes.get().toString(),
                city.get().toString(),
                state.get().toString(),
                repository.country ?: DEFAULT_COUNTRY
            )
        } else if (formType == UPDATE_FORM) {
            newRequirements = NewRequirement(
                requirementType.get().toString(),
                requirementNotes = extraNotes.get().toString(),
                city.get().toString(),
                state.get().toString(),
                repository.country ?: DEFAULT_COUNTRY,
                quantity.get().toString().trim().toInt(),
                landMark.get().toString(),
                null
            )
            if (lastRequirements == null || lastRequirements != newRequirements) {
                lastRequirements = newRequirements
                isUploaded = false
            }
        }

        _clickListener.value = FormUIClickListeners.ON_SUBMIT_CLICKED
    }

    private fun isValidForm(formType: FormType): Boolean {
        when {
            requirementType.get().toString().isBlank() -> {
                _formError.value = FormError.MISSING_REQUIREMENT_TYPE
                return false
            }
            state.get().toString().isBlank() -> {
                _formError.value = FormError.MISSING_STATE
                return false
            }
            city.get().toString().isBlank() -> {
                _formError.value = FormError.MISSING_CITY
                return false
            }
            quantity.get().toString().isBlank() -> {
                if (formType == UPDATE_FORM) {
                    _formError.value = FormError.MISSING_QUANTITIES
                    return false
                }
            }
            landMark.get().toString().isBlank() -> {
                if (formType == UPDATE_FORM) {
                    _formError.value = FormError.INVALID_LANDMARK
                    return false
                }
            }
        }
        return true
    }

    fun addRequirement() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (countryArray in dataSnapshot.children) {
                    if (countryArray.key.equals(repository.country, ignoreCase = true)) {
                        for (stateArray in countryArray.children) {
                            if (stateArray.key.equals(repository.state, ignoreCase = true)) {
                                for (cityArray in stateArray.children) {
                                    if (cityArray.key.equals(repository.city, ignoreCase = true)) {
                                        for (dataArray in cityArray.children) {
                                            try {
                                                val currentChild: Int = dataArray.key?.toInt() ?: 0
                                                uploadData((currentChild + 1).toString())
                                                return
                                            } catch (e: NumberFormatException) {
                                                e.printStackTrace()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    uploadData()
                    Log.d(
                        "firebase",
                        countryArray.key + ": " + countryArray.value.toString()
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        requirementDB.addValueEventListener(postListener)
    }

    private fun uploadData(childCount: String = VALUE_ZERO) {
        if (isUploaded) return
        isUploaded = true
        requirementDB.child(repository.country ?: "")
            .child(repository.state ?: "")
            .child(repository.city ?: "")
            .child(childCount).setValue(newRequirements)
            .addOnSuccessListener {
                _clickListener.value = ON_SUCCESS
            }
            .addOnFailureListener {
                _clickListener.value = ON_FAILURE
            }
    }


    public fun onAddAnotherContact() {
        _clickListener.value = FormUIClickListeners.ON_ADD_CONTACT_CLICKED
    }

    public fun onCountryClicked() {
        _clickListener.value = FormUIClickListeners.ON_COUNTRY_CLICKED
    }

    public fun onCityClicked() {
        _clickListener.value = FormUIClickListeners.ON_CITY_CLICKED
    }

    public fun onStateClicked() {
        _clickListener.value = FormUIClickListeners.ON_STATE_CLICKED
    }

    public fun onRequirementTypeClicked() {
        _clickListener.value = FormUIClickListeners.ON_REQUIREMENT_TYPE
    }
}