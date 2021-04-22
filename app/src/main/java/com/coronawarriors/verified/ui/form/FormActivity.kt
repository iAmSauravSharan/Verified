package com.coronawarriors.verified.ui.form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.coronawarriors.verified.R
import com.coronawarriors.verified.databinding.ActivityFormBinding
import com.coronawarriors.verified.databinding.ActivityHomeBinding
import com.coronawarriors.verified.ui.details.DetailsActivity
import com.coronawarriors.verified.ui.home.HomeViewModel

class FormActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityFormBinding
    private val mViewModel: FormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        launchDetailsPage()
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_form)
    }

    private fun launchDetailsPage() {
        startActivity(Intent(this, DetailsActivity::class.java))
        finish()
    }
}