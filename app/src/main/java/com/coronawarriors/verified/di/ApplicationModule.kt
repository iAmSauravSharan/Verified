package com.coronawarriors.verified.di

import android.content.Context
import com.coronawarriors.verified.data.DataRepository
import com.coronawarriors.verified.data.DataRepositoryImpl
import com.coronawarriors.verified.data.database.DatabaseManager
import com.coronawarriors.verified.data.database.DatabaseManagerImpl
import com.coronawarriors.verified.data.preference.PreferenceManager
import com.coronawarriors.verified.data.preference.PreferenceManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideRepository(databaseManager: DatabaseManager,
                          preferenceManager: PreferenceManager): DataRepository =
        DataRepositoryImpl(databaseManager, preferenceManager)

    @Provides
    @Singleton
    fun providePreference(@ApplicationContext context: Context): PreferenceManager = PreferenceManagerImpl(context)


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DatabaseManager = DatabaseManagerImpl(context)
}