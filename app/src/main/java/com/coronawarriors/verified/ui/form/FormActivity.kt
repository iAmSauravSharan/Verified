package com.coronawarriors.verified.ui.form

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import com.coronawarriors.verified.R
import com.coronawarriors.verified.data.DataRepository
import com.coronawarriors.verified.data.models.Contacts
import com.coronawarriors.verified.databinding.ActivityFormBinding
import com.coronawarriors.verified.enums.FormError.*
import com.coronawarriors.verified.enums.FormType
import com.coronawarriors.verified.enums.FormType.*
import com.coronawarriors.verified.enums.FormUIClickListeners.*
import com.coronawarriors.verified.ui.details.DetailsActivity
import com.coronawarriors.verified.ui.search.SearchActivity
import com.coronawarriors.verified.ui.settings.SettingsActivity
import com.coronawarriors.verified.util.Constants.FORM_PAYLOAD
import com.coronawarriors.verified.util.Constants.FORM_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_CITY_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_COUNTRY_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_FORM_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_REQUIREMENT_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_STATE_TYPE
import com.coronawarriors.verified.util.Utilities.sendShareIntent
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FormActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mBinding: ActivityFormBinding
    private val mViewModel: FormViewModel by viewModels()

    private lateinit var requirementRegisterLauncher: ActivityResultLauncher<Intent>
    private lateinit var countryRegisterLauncher: ActivityResultLauncher<Intent>
    private lateinit var cityRegisterLauncher: ActivityResultLauncher<Intent>
    private lateinit var stateRegisterLauncher: ActivityResultLauncher<Intent>
    @Inject
    lateinit var repository: DataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        getIntentData()
        setFormLayout()
        subscribeToClickListener()
        subscribeToFormError()
        registerActivityResultLaunchers()
    }

    private fun registerActivityResultLaunchers() {
        requirementRegisterLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null && data.hasExtra(SELECTED_REQUIREMENT_TYPE)) {
                        mBinding.tilRequirementType.editText?.setText(
                            data.getStringExtra(
                                SELECTED_REQUIREMENT_TYPE
                            )
                        )
                    }
                }
            }

        cityRegisterLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null && data.hasExtra(SELECTED_CITY_TYPE)) {
                        mBinding.tilCity.editText?.setText(
                            data.getStringExtra(
                                SELECTED_CITY_TYPE
                            )
                        )
                    }
                }
            }

        countryRegisterLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null && data.hasExtra(SELECTED_COUNTRY_TYPE)) {
                        mBinding.tilRequirementType.editText?.setText(
                            data.getStringExtra(
                                SELECTED_COUNTRY_TYPE
                            )
                        )
                    }
                }
            }

        stateRegisterLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null && data.hasExtra(SELECTED_STATE_TYPE)) {
                        mBinding.tilState.editText?.setText(
                            data.getStringExtra(
                                SELECTED_STATE_TYPE
                            )
                        )
                    }
                }
            }
    }

    private fun subscribeToFormError() {
        mViewModel.formError.observe(this, { error ->
            when (error) {
                MISSING_REQUIREMENT_TYPE -> showToast(resources.getString(R.string.missing_requirement_type))
                INVALID_EXTRA_NOTE -> showToast(resources.getString(R.string.invalid_extra_note))
                MISSING_CITY -> showToast(resources.getString(R.string.invalid_city))
                MISSING_STATE -> showToast(resources.getString(R.string.invalid_state))
                INVALID_LANDMARK -> showToast(resources.getString(R.string.invalid_landmark))
                MISSING_QUANTITIES -> showToast(resources.getString(R.string.invalid_quantities))
                MISSING_CONTACTS -> showToast(resources.getString(R.string.missing_contacts))
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeToClickListener() {
        mViewModel.clickListener.observe(this, { listeners ->
            val intent = Intent(this, SearchActivity::class.java)
            when (listeners) {
                ON_SUBMIT_CLICKED -> {
                    if (mViewModel.getFormType() == UPDATE_FORM) getContactList()
                    redirectToDetailsPage()
                }

                ON_REQUIREMENT_TYPE -> requirementRegisterLauncher.launch(intent)

                ON_CITY_CLICKED -> cityRegisterLauncher.launch(intent)

                ON_COUNTRY_CLICKED -> countryRegisterLauncher.launch(intent)

                ON_STATE_CLICKED -> stateRegisterLauncher.launch(intent)

                ON_ADD_CONTACT_CLICKED -> {
                    val inflatedLayout: View =
                        layoutInflater.inflate(R.layout.contact_layout, null, false)
                    inflatedLayout.findViewById<AppCompatImageView>(R.id.ivDelete)
                        .setOnClickListener {
                            mBinding.llContactDetails.removeView(inflatedLayout)
                        }
                    mBinding.llContactDetails.addView(inflatedLayout)
                }
            }
        })
    }

    private fun getContactList() {
        mViewModel.contactList.clear()
        val totalChildViews = mBinding.llContactDetails.childCount
        for (i in 0 until totalChildViews) {
            val view: View = mBinding.llContactDetails.getChildAt(i) as ViewGroup
            val contact = view.findViewById<TextInputLayout>(R.id.tilContact)
            val name = view.findViewById<TextInputLayout>(R.id.tilName)
            val address = view.findViewById<TextInputLayout>(R.id.tilPhysicalAddress)
            val onlineAddress = view.findViewById<TextInputLayout>(R.id.tilOnlineAddress)
            val isVerified = view.findViewById<AppCompatCheckBox>(R.id.cbVerified)
            val contacts = Contacts(
                phoneNumber = contact.editText?.text.toString().trim(),
                name = name.editText?.text.toString().trim(),
                address = address.editText?.text.toString().trim(),
                onlineAddress = onlineAddress.editText?.text.toString().trim(),
                isVerified = isVerified.isChecked
            )
            mViewModel.contactList.add(contacts)
        }
    }

    private fun setFormLayout() {
        mBinding.vgUploadDetails.visibility =
            if (mViewModel.getFormType() == SEARCH_FORM
                || mViewModel.getFormType() == VERIFY_FORM
            ) View.GONE else VISIBLE

        if (!repository.isLoggedIn) {
            val mySnackbar = Snackbar.make(
                mBinding.svMain,
                R.string.spread_the_word, Snackbar.LENGTH_LONG
            )
            mySnackbar.setAction(R.string.share) {
                sendShareIntent(this)
            }
            mySnackbar.show()
        }
    }

    private fun getIntentData() {
        if (intent != null && intent.hasExtra(SELECTED_FORM_TYPE))
            mViewModel.setFormType(intent.getSerializableExtra(SELECTED_FORM_TYPE) as FormType)
        mBinding.tvChangeCountry.text = String.format(
            "%s %s%s",
            resources.getString(R.string.not_from),
            repository.country?.toLowerCase(),
            resources.getString(R.string.change_country)
        )
        mBinding.tvProceed.text =
            when {
                mViewModel.getFormType() == SEARCH_FORM -> resources.getString(R.string.search)
                mViewModel.getFormType() == ADD_FORM -> resources.getString(R.string.upload)
                else -> resources.getString(R.string.update)
            }

        mBinding.include.tvScreenName.text = when {
            mViewModel.getFormType() == SEARCH_FORM -> resources.getString(R.string.search_requirement)
            mViewModel.getFormType() == ADD_FORM -> resources.getString(R.string.upload_details)
            else -> resources.getString(R.string.update_details)
        }
        mBinding.vgUploadDetails.visibility =
            if (mViewModel.getFormType() == UPDATE_FORM) VISIBLE
            else GONE
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_form)
        mBinding.viewModel = mViewModel
        mBinding.include.ivBack.setOnClickListener { onBackPressed() }
        mBinding.include.ivSettings.visibility = VISIBLE
        mBinding.include.ivBack.visibility = if (repository.isLoggedIn) VISIBLE else INVISIBLE
        mBinding.include.ivSettings.setOnClickListener { launchSettingsPage() }
    }

    private fun launchSettingsPage() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToDetailsPage() {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(FORM_TYPE, mViewModel.getFormType())
        intent.putExtra(FORM_PAYLOAD, if (mViewModel.getFormType() == SEARCH_FORM)
            mViewModel.getRequirement() else mViewModel.getNewRequirement())
        startActivity(intent)
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        Toast.makeText(
            this, resources.getString(R.string.signin_error),
            Toast.LENGTH_SHORT
        ).show()
    }
}