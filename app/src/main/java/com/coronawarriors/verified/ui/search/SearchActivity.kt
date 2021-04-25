package com.coronawarriors.verified.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.coronawarriors.verified.R
import com.coronawarriors.verified.data.DataRepositoryImpl
import com.coronawarriors.verified.databinding.ActivitySearchBinding
import com.coronawarriors.verified.databinding.ActivitySplashBinding
import com.coronawarriors.verified.ui.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySearchBinding
    private val mViewModel: SearchViewModel by viewModels()
    @Inject
    lateinit var repository: DataRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
    }
}