package com.eria.app

import android.app.Application
import android.content.Context
import com.eria.data.preferance.AppPrefs
import com.google.firebase.FirebaseApp
import javax.inject.Inject

class EriaApplication : Application() {

    @Inject
    lateinit var appPrefs: AppPrefs

    override fun onCreate() {
        super.onCreate()
        applicationInstance = this
        FirebaseApp.initializeApp(this)
    }


    fun saveNewFcmToken(mContext: Context,newFcmToken: String?) {
        appPrefs.setFcmToken(mContext,newFcmToken)
    }
    companion object {
        val TAG = EriaApplication::class.java.simpleName
        lateinit var appPrefs: AppPrefs

        @get:Synchronized
        var applicationInstance: EriaApplication? = null

        @JvmStatic
        fun getPrefs(): AppPrefs {
            return appPrefs
        }
    }
}