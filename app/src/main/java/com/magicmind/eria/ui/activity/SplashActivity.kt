package com.magicmind.eria.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.magicmind.eria.R
import com.magicmind.eria.databinding.ActivitySplashBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.magicmind.eria.app.EriaApplication
import com.magicmind.eria.ui.base.BaseActivity
import com.magicmind.eria.ui.base.HomeBaseActivity

class SplashActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private val SPLASH_TIMEOUT: Long = 2500
    private lateinit var binding: ActivitySplashBinding

    private var googleApiClient: GoogleApiClient? = null
    private val REQUESTLOCATION = 1000
    private lateinit var locationManager: LocationManager

    var location1: Location? = null

    // TODO: Step 1.1, Review variables (no changes).
    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e., how often you should receive
    // updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback

    // Used only for local storage of the last known location. Usually, this would be saved to your
    // database, but because this is a simplified sample without a full database, we only need the
    // last location to create a Notification if the user navigates away from the app.
    private var currentLocation: Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //binding.ivLogo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move))
        getFCMToken()
        displaySplashScreen()
    }


    fun displaySplashScreen() {
        val isLoggedIn: Boolean = EriaApplication.getPrefs().isUserLoggedIn(
            this@SplashActivity
        )
        if (isLoggedIn) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                getLocationPermission()
                return
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            getLocation()
        } else {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                val prefs: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(baseContext)
                val edit: SharedPreferences.Editor = prefs.edit()
                val previouslyStarted: Boolean =
                    prefs.getBoolean(getString(R.string.pref_previously_started), false)
                if (!previouslyStarted) {
                    val i = Intent(this@SplashActivity, TutorialActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                    startActivity(i)
                    finish()
                } else {
                    val i = Intent(this@SplashActivity, LoginActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                    startActivity(i)
                    finish()
                }


            }, SPLASH_TIMEOUT)
        }
    }

    fun getFCMToken(): String {
        var token = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
            EriaApplication.getPrefs().setFcmToken(
                this,
                token
            )
            // Log and toast
            //val msg = getString(R.string.msg_token_fmt, token)
            Log.e("TAG", token)
            // Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
        return token
    }

    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            getLocationPermission()
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableLoc()
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                try {
                    Log.e(this.javaClass.name, "${location?.latitude}+  ${location?.longitude}")

                    val i = Intent(this@SplashActivity, HomeBaseActivity::class.java)
                    i.putExtra("location", location)
                    overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                    startActivity(i)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }


    }

    private fun getLocationCallback() {
        // TODO: Step 1.4, Initialize the LocationCallback.
        locationCallback = object : LocationCallback() {
            @SuppressLint("LongLogTag", "MissingPermission")
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                if (locationResult?.lastLocation != null) {

                    // Normally, you want to save a new location to a database. We are simplifying
                    // things a bit and just saving it as a local variable, as we only need it again
                    // if a Notification is created (when the user navigates away from app).
                    currentLocation = locationResult.lastLocation
                    Log.e(
                        this.javaClass.name.toString(),
                        "LATITUDE " + currentLocation?.latitude + ",  LONGITUDE " + currentLocation?.longitude
                    )

                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest, locationCallback, Looper.myLooper()
                    )

                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                        try {
                            Log.e(
                                this.javaClass.name,
                                "${location?.latitude}+  ${location?.longitude}"
                            )

                            val i = Intent(this@SplashActivity, HomeBaseActivity::class.java)
                            i.putExtra("location", location)
                            overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                            startActivity(i)
                            finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                } else {
                    Log.e(this.javaClass.name.toString(), "Location missing in callback.")
                }
            }
        }
    }

    private fun enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this@SplashActivity)
                .addOnConnectionFailedListener(this@SplashActivity).build()
            googleApiClient?.connect()
            //getLocationRequest()
            locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (30 * 1000).toLong()
            locationRequest.fastestInterval = (5 * 1000).toLong()
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            // **************************
            builder.setAlwaysShow(true) // this is the key ingredient
            // **************************
            val result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build())
            result.setResultCallback(object : ResultCallback<LocationSettingsResult?> {
                override fun onResult(result: LocationSettingsResult) {
                    val status = result.status
                    val state = result
                        .locationSettingsStates

                    when (status.statusCode) {
                        LocationSettingsStatusCodes.SUCCESS -> {


                           /* if (ActivityCompat.checkSelfPermission(
                                    this@SplashActivity,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    this@SplashActivity,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return
                            }
                            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                                try {
                                    Log.e(this.javaClass.name, "${location?.latitude}+  ${location?.longitude}")

                                    val i = Intent(this@SplashActivity, HomeBaseActivity::class.java)
                                    i.putExtra("location", location )
                                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                                    startActivity(i)
                                    finish()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }*/
                            Toast.makeText(
                                this@SplashActivity,
                                "CONNECTED",
                                Toast.LENGTH_SHORT
                            ).show()
                            //getLocationCallback()
                        }
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                 // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                    this@SplashActivity,
                                    REQUESTLOCATION
                                )
                            } catch (e: IntentSender.SendIntentException) {
                                // Ignore the error.
                            }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
            })
        }
    }


    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@SplashActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@SplashActivity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
                )
            }
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                enableLoc()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                    if ((ContextCompat.checkSelfPermission(
                            this@SplashActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ==
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        enableLoc()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    //locationAlertDialog()
                }
                return
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUESTLOCATION -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(this.localClassName, "OK")
                    /*if (ActivityCompat.checkSelfPermission(
                            this@SplashActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this@SplashActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                        try {

                            if (location!=null) {
                                Log.e(
                                    this.javaClass.name,
                                    "${location?.latitude}+  ${location?.longitude}+ abcd"
                                )

                                val i = Intent(this@SplashActivity, HomeBaseActivity::class.java)
                                i.putExtra("location", location)
                                i.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                overridePendingTransition(
                                    R.anim.popup_in_anim,
                                    R.anim.popup_out_anim
                                )
                                startActivity(i)
                                finish()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }*/
                     getLocationCallback()
                    Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
                }
                Activity.RESULT_CANCELED -> Log.d(this.localClassName, "CANCEL")
            }
        }
    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onLocationChanged(location: Location) {

       /* val i = Intent(this@SplashActivity, HomeBaseActivity::class.java)
        i.putExtra("location", location)
        overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
        startActivity(i)
        finish()*/
        /*if (ActivityCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            try {
                if (location!=null) {
                    Log.e(this.javaClass.name, "${location?.latitude}+  ${location?.longitude} kn1234edsd")

                    val i = Intent(this@SplashActivity, HomeBaseActivity::class.java)
                    i.putExtra("location", location)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                    startActivity(i)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }*/
    }

    override fun onProviderDisabled(provider: String) {
        enableLoc()
    }

    override fun onProviderEnabled(provider: String) {
       // getLocationCallback()
        if (ActivityCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            try {
                if (location!=null) {

                   /* Log.e(this.javaClass.name, "${location?.latitude}+  ${location?.longitude} kn1234edsd")

                    val i = Intent(this@SplashActivity, HomeBaseActivity::class.java)
                    i.putExtra("location", location)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                    startActivity(i)
                    finish()*/
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        // super.onProviderEnabled(provider)
    }
}