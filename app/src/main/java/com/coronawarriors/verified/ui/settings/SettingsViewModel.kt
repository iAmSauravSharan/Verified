package com.coronawarriors.verified.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coronawarriors.verified.data.DataRepository
import com.coronawarriors.verified.enums.SettingsUIClickListener
import com.coronawarriors.verified.enums.SettingsUIClickListener.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val repository: DataRepository) : ViewModel() {

    private val _clickListener = MutableLiveData<SettingsUIClickListener>()
    val clickListener: LiveData<SettingsUIClickListener>
        get() = _clickListener

    public fun onLoginClicked() {
        if (!repository.isLoggedIn) _clickListener.value = LOGIN
        else {
            Firebase.auth.signOut()
            if (Firebase.auth.currentUser == null) {
                repository.isLoggedIn = false
                _clickListener.value = LOGOUT
            }
        }
    }

    public fun onShareUsClicked() {
        _clickListener.value = SHARE_US
    }

    public fun onContactUsClicked() {
        _clickListener.value = CONTACT_US
    }

}