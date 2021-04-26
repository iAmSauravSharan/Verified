package com.coronawarriors.verified.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coronawarriors.verified.data.DataRepository
import com.coronawarriors.verified.enums.FormType
import com.coronawarriors.verified.enums.HomeUIClickListener
import com.coronawarriors.verified.enums.HomeUIClickListener.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repository: DataRepository

    private val _clickListener = MutableLiveData<HomeUIClickListener>()
    val clickListener: LiveData<HomeUIClickListener>
        get() = _clickListener

    private val _formType = MutableLiveData<FormType>()
    val formType: LiveData<FormType>
        get() = _formType

    public fun onLogoutClicked() {
        Firebase.auth.signOut()
        if (Firebase.auth.currentUser == null) {
            repository.isLoggedIn = false
            _clickListener.value = ON_LOGOUT_CLICKED
        }
    }

    public fun onMotivateClicked() {
        _clickListener.value = ON_MOTIVATE_CLICKED
    }

    public fun onShareUsClicked() {
        _clickListener.value = ON_SHARE_US_CLICKED
    }

    public fun onContactUsClicked() {
        _clickListener.value = ON_CONTACT_US_CLICKED
    }

    public fun onAddResourcesClicked() {
        _clickListener.value = ON_ADD_RESOURCES_CLICKED
    }

    public fun onUpdateContactsClicked() {
        _clickListener.value = ON_UPDATE_CONTACTS_CLICKED
    }

    public fun onVerifyContactsClicked() {
        _clickListener.value = ON_VERIFY_CONTACTS_CLICKED
    }

    public fun onSearchClicked() {
        _clickListener.value = ON_SEARCH_CLICKED
    }
}