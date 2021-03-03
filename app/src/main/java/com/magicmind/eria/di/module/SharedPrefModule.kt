package com.magicmind.eria.di.module

import android.content.Context
import android.content.SharedPreferences
import com.magicmind.eria.app.EriaApplication
import com.magicmind.eria.data.preferance.AppPrefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(application: EriaApplication): SharedPreferences =
        application.getSharedPreferences("com.magicmind.eria.appdata", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSharedPreferenceClient(sharedPreferences: SharedPreferences): AppPrefs {
        return AppPrefs()
    }


}