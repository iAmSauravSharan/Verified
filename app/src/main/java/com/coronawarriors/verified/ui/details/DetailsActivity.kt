package com.coronawarriors.verified.ui.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.coronawarriors.verified.R
import com.coronawarriors.verified.databinding.ActivityDetailsBinding
import com.coronawarriors.verified.databinding.ActivityHomeBinding
import com.coronawarriors.verified.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDetailsBinding
    private val mViewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        launchDetailsPage()
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
    }

    private fun launchDetailsPage() {
        startActivity(Intent(this, DetailsActivity::class.java))
        finish()
    }
}