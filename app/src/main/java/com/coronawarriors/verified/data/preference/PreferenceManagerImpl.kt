package com.coronawarriors.verified.data.preference

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferenceManagerImpl(@ApplicationContext private val context: Context): PreferenceManager {

    companion object {
        const val APP_LAUNCH_STATUS = "app_launch_status"
    }

    private val name: String = "preferences"
    private val sharedPreferences =
        if (Build.VERSION.SDK_INT >= 23) {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            EncryptedSharedPreferences.create(
                name,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }

    override var isAppLaunchedForTheFirstTime: Boolean by BooleanPreference(
        sharedPreferences, APP_LAUNCH_STATUS, true)

}


class BooleanPreference(
    private var preferences: SharedPreferences,
    private var name: String,
    private var defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {
    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): Boolean {
        return preferences.getBoolean(name, defaultValue)
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: Boolean
    ) {
        preferences.edit().putBoolean(name, value).apply()
    }
}

class StringPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: String? = null
) : ReadWriteProperty<Any, String?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): String? =
        sharedPreferences.getString(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) =
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
}

class IntPreference(
    private var preferences: SharedPreferences,
    private var name: String,
    private var defaultValue: Int
) : ReadWriteProperty<Any, Int> {
    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): Int {
        return preferences.getInt(name, defaultValue)
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: Int
    ) {
        preferences.edit().putInt(name, value).apply()
    }
}

class LongPreference(
    private var preferences: SharedPreferences,
    private var name: String,
    private var defaultValue: Long
) : ReadWriteProperty<Any, Long> {
    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): Long {
        return preferences.getLong(name, defaultValue)
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: Long
    ) {
        preferences.edit().putLong(name, value).apply()
    }
}