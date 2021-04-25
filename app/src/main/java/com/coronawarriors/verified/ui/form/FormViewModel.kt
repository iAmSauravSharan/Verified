package com.coronawarriors.verified.ui.form

import androidx.lifecycle.ViewModel
import com.coronawarriors.verified.enums.FormType
import com.coronawarriors.verified.enums.FormType.SEARCH_FORM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(): ViewModel() {

    private var formType =  SEARCH_FORM

    fun setFormType(type: FormType) {
        this.formType = type
    }

    fun getFormType(): FormType = this.formType


}