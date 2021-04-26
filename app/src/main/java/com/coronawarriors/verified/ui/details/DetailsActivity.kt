package com.coronawarriors.verified.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.coronawarriors.verified.R
import com.coronawarriors.verified.databinding.ActivityDetailsBinding
import com.coronawarriors.verified.enums.FormType
import com.coronawarriors.verified.ui.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDetailsBinding
    private val mViewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        mBinding.include.ivBack.setOnClickListener{ onBackPressed() }
        mBinding.include.ivSettings.visibility = View.VISIBLE
        mBinding.include.ivSettings.setOnClickListener { launchSettingsPage() }
        mBinding.include.tvScreenName.text = resources.getString(R.string.details)
    }

    private fun launchSettingsPage() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}