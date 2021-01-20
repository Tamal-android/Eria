package com.eria.data.preferance

import android.content.Context
import android.content.SharedPreferences

class AppPrefs : DPrefs() {

    private val PREF_KEY_USER_ID = "KEY_USER_ID"
    private val PREF_KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
    private val PREF_KEY_SIGN_IN_MOBILE = "KEY_SIGN_IN_MOBILE"
    private val PREF_KEY_USER_EMAIL = "KEY_USER_EMAIL"
    private val PREF_KEY_LOGGED_IN = "KEY_LOGGED_IN"
    private val PREF_KEY_SHARE_CODE = "KEY_SHARE_CODE"
    private val PREF_KEY_FCM_TOKEN = "KEY_FCM_TOKEN"
    private val PREF_KEY_IS_FIRST_TIME = "KEY_IS_FIRST_TIME"
    private val PREF_KEY_USER_NAME = "KEY_USER_NAME"
    private val PREF_KEY_USER_TYPE = "KEY_USER_TYPE"

    private val PREF_DEFAULT_USER_ID = ""
    private val PREF_DEFAULT_AUTH_TOKEN = ""
    private val PREF_DEFAULT_SIGN_IN_MOBILE = ""
    private val PREF_DEFAULT_USER_EMAIL = ""
    private val PREF_DEFAULT_LOGGED_IN = false
    private val PREF_DEFAULT_SHARE_CODE = ""
    private val PREF_DEFAULT_FCM_TOKEN = ""
    private val PREF_DEFAULT_FIRST_TIME = true
    private val PREF_DEFAULT_USER_NAME = ""
    private val PREF_DEFAULT_USER_TYPE = ""


    fun setIsFirstTimeUser(mContext: Context, boolean: Boolean) {
        setBoolean(mContext,PREF_KEY_IS_FIRST_TIME, boolean)
    }

    fun isFirstTimeUser(mContext: Context): Boolean {
        return getBoolean(mContext,PREF_KEY_IS_FIRST_TIME, PREF_DEFAULT_FIRST_TIME)
    }

    fun setUserLoggedIn(mContext: Context,isLoggedIn: Boolean) {
        setBoolean(mContext,PREF_KEY_LOGGED_IN, isLoggedIn)
    }

    fun isUserLoggedIn(mContext: Context): Boolean {
        return getBoolean(mContext,PREF_KEY_LOGGED_IN, PREF_DEFAULT_LOGGED_IN)
    }


    fun setUserId(mContext: Context,userHash: String?) {
        userHash?.let { setString(mContext,PREF_KEY_USER_ID, it) }
    }

    fun getUserId(mContext: Context): String? {
        return getString(mContext,PREF_KEY_USER_ID, PREF_DEFAULT_USER_ID)
    }

    fun setUserName(mContext: Context,userName: String?) {
        userName?.let { setString(mContext,PREF_KEY_USER_NAME, it) }
    }

    fun getUserName(mContext: Context): String? {
        return getString(mContext,PREF_KEY_USER_NAME, PREF_DEFAULT_USER_NAME)
    }

    fun setUserEmail(mContext: Context,userEmail: String?) {
        userEmail?.let { setString(mContext,PREF_KEY_USER_EMAIL, it) }
    }

    fun getUserEmail(mContext: Context): String? {
        return getString(mContext,PREF_KEY_USER_EMAIL, PREF_DEFAULT_USER_EMAIL)
    }

    fun setSignInMobile(mContext: Context,signInMobile: String?) {
        signInMobile?.let { setString(mContext,PREF_KEY_SIGN_IN_MOBILE, it) }
    }

    fun getSignInMobile(mContext: Context): String? {
        return getString(mContext,PREF_KEY_SIGN_IN_MOBILE, PREF_DEFAULT_SIGN_IN_MOBILE)
    }

    fun setFcmToken(mContext: Context,fcmToken: String?) {
        fcmToken?.let { setString(mContext,PREF_KEY_FCM_TOKEN, it) }
    }

    fun getFcmToken(mContext: Context): String? {
        return getString(mContext,PREF_KEY_FCM_TOKEN, PREF_DEFAULT_FCM_TOKEN)
    }
}
