package com.coronawarriors.verified.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.coronawarriors.verified.R
import com.coronawarriors.verified.data.models.Requirement
import com.coronawarriors.verified.databinding.ActivityDetailsBinding
import com.coronawarriors.verified.enums.DetailsType
import com.coronawarriors.verified.enums.DetailsType.*
import com.coronawarriors.verified.ui.settings.SettingsActivity
import com.coronawarriors.verified.util.Constants
import com.coronawarriors.verified.util.Constants.FORM_PAYLOAD
import com.coronawarriors.verified.util.Constants.FORM_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_DETAILS_TYPE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDetailsBinding
    private val mViewModel: DetailsViewModel by viewModels()
    private lateinit var adapter: DetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        getIntentData()
        getApiData()
        subscribeToResult()
    }

    private fun getApiData() {
        when (mViewModel.getDetailsType()) {
            SEARCH_REQUIREMENTS -> {
                mViewModel.fetchDetails()
            }
            VERIFY_REQUIREMENTS -> {

            }
            HELP_REQUIREMENTS -> {

            }
        }
    }

    private fun subscribeToResult() {
        mViewModel.searchResult.observe(this, { result ->
            mBinding.loading.visibility = GONE
            mBinding.vgData.visibility = if (result.isNullOrEmpty()) GONE else VISIBLE
            mBinding.vgStatus.visibility = if (result.isNullOrEmpty()) VISIBLE else GONE
            if (!result.isNullOrEmpty()) adapter.updateDetails(result)
        })
    }

    private fun getIntentData() {
        if (intent != null) {
            if (intent.hasExtra(SELECTED_DETAILS_TYPE)) {
                mViewModel.setDetailsType(intent.getSerializableExtra(SELECTED_DETAILS_TYPE) as DetailsType)
            }
            if (mViewModel.getDetailsType() == SEARCH_REQUIREMENTS) {
                mViewModel.setRequirement(intent.getParcelableExtra<Requirement>(FORM_PAYLOAD) as Requirement)
            }
            adapter = DetailsAdapter(mViewModel.getDetailsType() == SEARCH_REQUIREMENTS)
            mBinding.rvDetailsList.adapter = adapter
        }
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        mBinding.include.ivBack.setOnClickListener { onBackPressed() }
        mBinding.include.ivSettings.visibility = View.VISIBLE
        mBinding.include.ivSettings.setOnClickListener { launchSettingsPage() }
        mBinding.include.tvScreenName.text = resources.getString(R.string.details)
    }

    private fun launchSettingsPage() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}