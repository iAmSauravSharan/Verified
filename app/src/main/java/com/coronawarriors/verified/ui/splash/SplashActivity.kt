package com.coronawarriors.verified.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.coronawarriors.verified.R
import com.coronawarriors.verified.data.DataRepositoryImpl
import com.coronawarriors.verified.databinding.ActivitySplashBinding
import com.coronawarriors.verified.ui.form.FormActivity
import com.coronawarriors.verified.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding
    private val mViewModel: SplashViewModel by viewModels()
    @Inject lateinit var repository: DataRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        launchNextScreen()
    }

    private fun launchNextScreen() {
        Timer().schedule(1700) {
            if (repository.isAppLaunchedForTheFirstTime || !repository.isLoggedIn) launchFormPage()
            else if (repository.isLoggedIn) launchHomePage()
        }
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    private fun launchHomePage() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }


    private fun launchFormPage() {
        startActivity(Intent(this, FormActivity::class.java))
        finish()
    }
}