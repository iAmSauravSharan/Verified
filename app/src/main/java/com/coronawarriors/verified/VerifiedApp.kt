package com.coronawarriors.verified

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VerifiedApp : Application() {

        fun getInstance(): Context = applicationContext

}