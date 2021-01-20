package com.eria.di.module

import android.content.Context
import android.content.SharedPreferences
import com.eria.app.EriaApplication
import com.eria.data.preferance.AppPrefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(application: EriaApplication): SharedPreferences =
        application.getSharedPreferences("com.eria.appdata", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSharedPreferenceClient(sharedPreferences: SharedPreferences): AppPrefs {
        return AppPrefs()
    }


}