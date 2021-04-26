package com.coronawarriors.verified.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.coronawarriors.verified.BuildConfig
import com.coronawarriors.verified.R
import com.coronawarriors.verified.databinding.ActivityHomeBinding
import com.coronawarriors.verified.enums.DetailsType
import com.coronawarriors.verified.enums.DetailsType.VERIFY_REQUIREMENTS
import com.coronawarriors.verified.enums.FormType
import com.coronawarriors.verified.enums.FormType.*
import com.coronawarriors.verified.enums.HomeUIClickListener.*
import com.coronawarriors.verified.ui.details.DetailsActivity
import com.coronawarriors.verified.ui.form.FormActivity
import com.coronawarriors.verified.ui.settings.SettingsActivity
import com.coronawarriors.verified.ui.splash.SplashActivity
import com.coronawarriors.verified.util.Constants.EMPTY_STRING
import com.coronawarriors.verified.util.Constants.SELECTED_DETAILS_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_FORM_TYPE
import com.coronawarriors.verified.util.Utilities.sendShareIntent
import dagger.hilt.android.AndroidEntryPoint
import java.lang.String
import java.util.*


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityHomeBinding
    private val mViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        subscribeToClickListener()
    }

    private fun subscribeToClickListener() {
        mViewModel.clickListener.observe(this, { listener ->
            when (listener) {
                ON_SEARCH_CLICKED -> launchFormScreen(SEARCH_FORM)
                ON_VERIFY_CONTACTS_CLICKED -> launchVerificationsPage()
                ON_UPDATE_CONTACTS_CLICKED -> launchUpdateContactsPage()
                ON_ADD_RESOURCES_CLICKED -> launchAddResourcePage()
                ON_LOGOUT_CLICKED -> launchSplashScreen()
                ON_SETTINGS_CLICKED -> launchSettingsPage()
                ON_MOTIVATE_CLICKED -> openMotivationScreen()
            }
        })
    }

    private fun openMotivationScreen() {

    }

    private fun launchSplashScreen() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    private fun launchAddResourcePage() {
        launchFormScreen(ADD_FORM)
    }

    private fun launchUpdateContactsPage() {
        launchFormScreen(UPDATE_FORM)
    }

    private fun launchVerificationsPage() {
        launchDetailsPage()
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        mBinding.viewModel = mViewModel
        mBinding.include.ivBack.visibility = GONE
        mBinding.include.ivSettings.visibility = VISIBLE
        mBinding.include.ivSettings.setOnClickListener { launchSettingsPage() }
        mBinding.include.tvScreenName.text = resources.getText(R.string.home)
    }

    private fun launchDetailsPage() {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(SELECTED_DETAILS_TYPE, VERIFY_REQUIREMENTS)
        startActivity(intent)
    }

    private fun launchFormScreen(formType: FormType) {
        val intent = Intent(this, FormActivity::class.java)
        intent.putExtra(SELECTED_FORM_TYPE, formType)
        startActivity(intent)
    }

    private fun launchSettingsPage() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
