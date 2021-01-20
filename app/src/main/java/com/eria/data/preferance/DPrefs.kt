package com.eria.data.preferance

import android.content.Context
import android.content.SharedPreferences
import com.eria.utils.DCryptUtil

/**
 * DPrefs
 *
 * @author Dipanjan Chakraborty
 */

open class DPrefs() {

    private val FILE_NAME = "com.eria"
    /**
     * Store some string in SharedPreferences using a key value and the data
     *
     * @param key
     * @param value
     */

    protected fun setString(mContext: Context,key: String, value: String) {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, getEncryptedData(value)).apply()
    }

    /**
     * Get string value from SharedPreferences using key value
     *
     * @param key
     * @param def
     * @return a string
     */

    protected fun getString(mContext: Context,key: String, def: String): String? {
        val prefs = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return prefs.getString(key, def).toString()
    }

    /**
     * Store some boolean value in SharedPreferences using a key value and the
     * data
     *
     * @param key
     * @param value
     */

    protected fun setBoolean(mContext: Context,key: String, value: Boolean) {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    /**
     * Get boolean value from SharedPreferences using key value
     *
     * @param key
     * @param def
     * @return a boolean value
     */

    protected fun getBoolean(mContext: Context,key: String, def: Boolean): Boolean {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, def)
    }

    /**
     * Store some integer value in SharedPreferences using a key value and the
     * data
     *
     * @param key
     * @param value
     */

    protected fun setInt(mContext: Context,key: String, value: Int) {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(key, value).apply()
    }

    /**
     * Get integer value from SharedPreferences using key value
     *
     * @param key
     * @param def
     * @return a integer value
     */

    protected fun getInt(mContext: Context,key: String, def: Int): Int {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, def)
    }

    /**
     * Store some Long value in SharedPreferences using a key value and the data
     *
     * @param key
     * @param value
     */

    protected fun setLong(mContext: Context,key: String, value: Long) {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong(key, value).apply()
    }

    /**
     * Get Long value from SharedPreferences using key value
     *
     * @param key
     * @param def
     * @return a Long value
     */

    protected fun getLong(mContext: Context,key: String, def: Long): Long {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(key, def)
    }

    /**
     * Store some Double value in SharedPreferences using a key value and the
     * data
     *
     * @param key
     * @param value
     */

    protected fun setDouble(mContext: Context,key: String, value: Double) {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, java.lang.Double.toString(value)).apply()
    }

    /**
     * Get Double value from SharedPreferences using key value
     *
     * @param key
     * @param def
     * @return a Double value
     */

    protected fun getDouble(mContext: Context,key: String, def: Double): Double {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return java.lang.Double.parseDouble(
            sharedPreferences.getString(
                key,
                java.lang.Double.toString(def)
            ).toString()
        )
    }


    /**
     * Store some Float value in SharedPreferences using a key value and the
     * data
     *
     * @param key
     * @param value
     */

    protected fun setFloat(mContext: Context,key: String, value: Float) {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    /**
     * Get Float value from SharedPreferences using key value
     *
     * @param key
     * @param def
     * @return a Float value
     */

    protected fun getFloat(mContext: Context,key: String, def: Float): Float {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getFloat(key, def)
    }

    fun clearPreference(mContext: Context,) {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    private fun getDecryptedDataFromPref(mContext: Context, key: String, defValue: String): String? {
        val sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val data = sharedPreferences.getString(key, defValue)
        return DCryptUtil.decrypt(DCryptUtil.PASSWORD, data)
    }

    private fun getEncryptedData(value: String?): String? {
        var data = value
        value?.let { text ->
            data = DCryptUtil.encrypt(DCryptUtil.PASSWORD, text)
        }
        return data
    }
}
