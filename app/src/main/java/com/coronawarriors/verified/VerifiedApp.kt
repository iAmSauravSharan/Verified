package com.coronawarriors.verified

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VerifiedApp : Application() {

        override fun onCreate() {
                super.onCreate()
        }

}