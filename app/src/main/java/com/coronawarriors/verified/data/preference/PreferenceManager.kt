package com.coronawarriors.verified.data.preference

import android.content.Context

interface PreferenceManager {

    var isAppLaunchedForTheFirstTime: Boolean
    var isLoggedIn: Boolean
    var country: String?
    var accountId: String?
    var accountIdToken: String?

}