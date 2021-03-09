package com.magicmind.eria.data.preferance

import android.content.Context
import android.content.SharedPreferences

class AppPrefs : DPrefs() {

    private val PREF_KEY_USER_ID = "KEY_USER_ID"
    private val PREF_KEY_OTP = "KEY_OTP"
    private val PREF_KEY_MOBILE_NO = "KEY_MOBILE_NO"
    private val PREF_KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
    private val PREF_KEY_SIGN_IN_MOBILE = "KEY_SIGN_IN_MOBILE"
    private val PREF_KEY_USER_EMAIL = "KEY_USER_EMAIL"
    private val PREF_KEY_LOGGED_IN = "KEY_LOGGED_IN"
    private val PREF_KEY_SHARE_CODE = "KEY_SHARE_CODE"
    private val PREF_KEY_FCM_TOKEN = "KEY_FCM_TOKEN"
    private val PREF_KEY_IS_FIRST_TIME = "KEY_IS_FIRST_TIME"
    private val PREF_KEY_USER_NAME = "KEY_USER_NAME"
    private val PREF_KEY_USER_TYPE = "KEY_USER_TYPE"
    private val PREF_KEY_CURRENT_LATITUDE = "KEY_CURRENT_LATITUDE"
    private val PREF_KEY_CURRENT_LONGITUDE = "KEY_CURRENT_LONGITUDE"
    private val PREF_KEY_ADDRESS_ID = "KEY_ADDRESS_ID"
    private val PREF_KEY_BEARER_TOKEN = "KEY_BEARER_TOKEN"
    private val PREF_KEY_STATUS = "KEY_STATUS"

    private val PREF_DEFAULT_USER_ID = 0
    private val PREF_DEFAULT_OTP = ""
    private val PREF_DEFAULT_MOBILE_NO = ""
    private val PREF_DEFAULT_AUTH_TOKEN = ""
    private val PREF_DEFAULT_SIGN_IN_MOBILE = ""
    private val PREF_DEFAULT_USER_EMAIL = ""
    private val PREF_DEFAULT_LOGGED_IN = false
    private val PREF_DEFAULT_SHARE_CODE = ""
    private val PREF_DEFAULT_FCM_TOKEN = ""
    private val PREF_DEFAULT_FIRST_TIME = true
    private val PREF_DEFAULT_USER_NAME = ""
    private val PREF_DEFAULT_USER_TYPE = ""
    private val PREF_DEFAULT_CURRENT_LATITUDE = ""
    private val PREF_DEFAULT_CURRENT_LONGITUDE = ""
    private val PREF_DEFAULT_ADDRESS_ID = 0
    private val PREF_DEFAULT_BEARER_TOKEN = ""
    private val PREF_DEFAULT_STATUS = ""


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


    fun setUserId(mContext: Context,userHash: Int?) {
        userHash?.let { setInt(mContext,PREF_KEY_USER_ID, it) }
    }

    fun getUserId(mContext: Context): Int? {
        return getInt(mContext,PREF_KEY_USER_ID, PREF_DEFAULT_USER_ID)
    }

    fun setOTP(mContext: Context,otpHash: String?) {
        otpHash?.let { setString(mContext,PREF_KEY_OTP, it) }
    }

    fun getOTP(mContext: Context): String? {
        return getString(mContext,PREF_KEY_OTP, PREF_DEFAULT_OTP)
    }
    fun setMobile_no(mContext: Context,mobileHash: String?) {
        mobileHash?.let { setString(mContext,PREF_KEY_MOBILE_NO, it) }
    }

    fun getMobile_no(mContext: Context): String? {
        return getString(mContext,PREF_KEY_MOBILE_NO, PREF_DEFAULT_MOBILE_NO)
    }
    fun setBEARER_TOKEN(mContext: Context,bearerHash: String?) {
        bearerHash?.let { setString(mContext,PREF_KEY_BEARER_TOKEN, it) }
    }

    fun getBEARER_TOKEN(mContext: Context): String? {
        return getString(mContext,PREF_KEY_BEARER_TOKEN, PREF_DEFAULT_BEARER_TOKEN)
    }
    fun setSTATUS(mContext: Context,statusHash: String?) {
        statusHash?.let { setString(mContext,PREF_KEY_STATUS, it) }
    }

    fun getSTATUS(mContext: Context): String? {
        return getString(mContext,PREF_KEY_STATUS, PREF_DEFAULT_STATUS)
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
    fun setCurrentLatitude(mContext: Context,latitude: String?) {
        latitude?.let { setString(mContext,PREF_KEY_CURRENT_LATITUDE, it) }
    }

    fun getCurrentLatitude(mContext: Context): String? {
        return getString(mContext,PREF_KEY_CURRENT_LATITUDE, PREF_DEFAULT_CURRENT_LATITUDE)
    }
    fun setCurrentLongitude(mContext: Context,longitude: String?) {
        longitude?.let { setString(mContext,PREF_KEY_CURRENT_LATITUDE, it) }
    }

    fun getCurrentLongitude(mContext: Context): String? {
        return getString(mContext,PREF_KEY_CURRENT_LONGITUDE, PREF_DEFAULT_CURRENT_LONGITUDE)
    }

    fun setAddressId(mContext: Context,addressId: Int?) {
        addressId?.let { setInt(mContext,PREF_KEY_ADDRESS_ID, it) }
    }

    fun getAddressId(mContext: Context): Int {
        return getInt(mContext,PREF_KEY_ADDRESS_ID, PREF_DEFAULT_ADDRESS_ID)
    }
}
