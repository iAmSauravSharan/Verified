package com.coronawarriors.verified

import android.app.Application
import com.coronawarriors.verified.util.Constants.CORONA_WARRIORS
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class VerifiedApp : Application() {

    companion object {
        lateinit var coronaWarriorsDB: FirebaseDatabase
    }

    override fun onCreate() {
        super.onCreate()
        initWarriorsDb()
    }

    private fun initWarriorsDb() {
        val options = FirebaseOptions.Builder()
            .setApiKey(com.coronawarriors.verified.BuildConfig.API_KEY)
            .setApplicationId(BuildConfig.APPLICATION_ID)
            .setDatabaseUrl(com.coronawarriors.verified.BuildConfig.DATABASE_URL)
            .build()
        val secondDB = FirebaseApp.initializeApp(applicationContext, options, CORONA_WARRIORS)
        coronaWarriorsDB = FirebaseDatabase.getInstance(secondDB)
        coronaWarriorsDB.reference.setValue(ServerValue.TIMESTAMP)
    }

}