package com.coronawarriors.verified.data

import com.coronawarriors.verified.data.database.DatabaseManager
import com.coronawarriors.verified.data.preference.PreferenceManager

interface DataRepository: DatabaseManager, PreferenceManager {



}