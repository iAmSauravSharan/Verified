package com.coronawarriors.verified.ui.form

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coronawarriors.verified.data.DataRepository
import com.coronawarriors.verified.data.database.entity.Contact
import com.coronawarriors.verified.data.models.Contacts
import com.coronawarriors.verified.data.models.NewRequirement
import com.coronawarriors.verified.data.models.Requirement
import com.coronawarriors.verified.enums.FormError
import com.coronawarriors.verified.enums.FormType
import com.coronawarriors.verified.enums.FormType.SEARCH_FORM
import com.coronawarriors.verified.enums.FormType.UPDATE_FORM
import com.coronawarriors.verified.enums.FormUIClickListeners
import com.coronawarriors.verified.util.Constants.DEFAULT_COUNTRY
import com.coronawarriors.verified.util.Constants.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private var formType = SEARCH_FORM
    private val _clickListener = MutableLiveData<FormUIClickListeners>()
    val clickListener: LiveData<FormUIClickListeners>
        get() = _clickListener

    private val _formError = MutableLiveData<FormError>()
    val formError: LiveData<FormError>
        get() = _formError

    public val requirementType: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val extraNotes: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val state: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val city: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val landMark: ObservableField<String> = ObservableField(EMPTY_STRING)
    public val quantity: ObservableField<String> = ObservableField(EMPTY_STRING)

    private lateinit var requirements: Requirement
    private lateinit var newRequirements: NewRequirement
    lateinit var contactList: ArrayList<Contacts>

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
                contactList
            )
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