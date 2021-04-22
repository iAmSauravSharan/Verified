package com.coronawarriors.verified.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.coronawarriors.verified.R
import com.coronawarriors.verified.databinding.ActivitySplashBinding
import com.coronawarriors.verified.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding
    private val mViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        redirectToHomePage()
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    private fun redirectToHomePage() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}