package com.coronawarriors.verified.data.preference

interface PreferenceManager {

    var isAppLaunchedForTheFirstTime: Boolean
    var isLoggedIn: Boolean
    var country: String?
    var userName: String?
    var accountId: String?
    var accountIdToken: String?

}