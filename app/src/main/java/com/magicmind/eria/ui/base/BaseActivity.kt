package com.magicmind.eria.ui.base

import android.graphics.Rect
import android.os.Bundle
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.magicmind.eria.R
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.room.Room
import com.dc.dlocation.LocationManager
import com.dc.dlocation.configuration.Configurations
import com.dc.dlocation.configuration.LocationConfiguration
import com.dc.dlocation.listener.LocationListener
import com.magicmind.eria.data.network.ApiClient
import com.magicmind.eria.data.network.ApiStores
import com.magicmind.eria.db_dao.AppDatabase

import java.util.regex.Pattern

open class BaseActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private var locationManager: LocationManager? = null
    private fun getLocationConfiguration(): LocationConfiguration {
        return Configurations.defaultConfiguration(
            "Please grant the permission to continue with Eria",
            "Would you mind to turn GPS on? Please turn the GPS on."
        )
    }

    var db:AppDatabase? = null
    override fun onStart() {
        super.onStart()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Address_Table"
        ).build()
    }
    protected open fun getLocationManager(): LocationManager? {
        return locationManager
    }

    open fun getLocation() {
        locationManager!!.get()
    }

    @CallSuper
    override fun onDestroy() {
        locationManager?.onDestroy()
        super.onDestroy()
    }

    @CallSuper
    override fun onPause() {
        locationManager?.onPause()
        super.onPause()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        locationManager?.onResume()
    }

    @CallSuper
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (locationManager != null)
            locationManager!!.onActivityResult(requestCode, resultCode, data)
    }

    @CallSuper
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        hideSoftKeyboard()
        overridePendingTransitionEnter()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        hideSoftKeyboard()
        overridePendingTransitionEnter()
    }


    fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }


    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun isConnected(context: Context): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun showProgress(progressMsg: String) {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(progressMsg)
        progressDialog!!.setCancelable(false)
        progressDialog!!.isIndeterminate = true
        progressDialog!!.show()
    }

    fun hideProgress() {
        if (progressDialog != null) {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
                progressDialog = null
            }
        }
    }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    fun hideSoftKeyboard() {
        try {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager!!.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getWebService(): ApiStores {
        return ApiClient.retrofit(this@BaseActivity, getString(R.string.base_url_api))!!.create(
            ApiStores::class.java
        )
    }


    fun initLocation(listener: LocationListener) {
        locationManager = LocationManager.Builder(applicationContext)
            .configuration(getLocationConfiguration())
            .activity(this)
            .notify(listener)
            .build()
    }

    fun <T : View> Activity.bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy { findViewById<T>(res) }
    }
}