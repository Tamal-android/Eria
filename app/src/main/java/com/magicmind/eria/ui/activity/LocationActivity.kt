package com.magicmind.eria.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationListener
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.magicmind.eria.R
import com.magicmind.eria.databinding.ActivityLocationBinding
import com.magicmind.eria.ui.base.HomeBaseActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.magicmind.eria.ui.base.BaseActivity
import java.util.*


class LocationActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private lateinit var binding: ActivityLocationBinding

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
    private var latLng: LatLng? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location)

        if (intent.getParcelableExtra<LatLng>("latlang")!=null){
            latLng=intent.getParcelableExtra<LatLng>("latlang")

            binding.etCustomLocation.text = getAddress(latLng!!)
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            getLocationPermission()

            return
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        getLocationPermission()
        binding.btnMyLocation.setOnClickListener(View.OnClickListener {

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                enableLoc()
            } else {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    try {
                        Log.e(this.javaClass.name,"${location?.latitude}+  ${location?.longitude}")
                        val i=Intent(this,MapsActivity::class.java).putExtra("type","location").putExtra("Latitude",location?.latitude).putExtra("Longitude",location?.longitude)
                        overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                        startActivity(i)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }

                }
            }
        })
        binding.etCustomLocation.setOnClickListener(View.OnClickListener {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                enableLoc()
            } else {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->

                    try {
                        Log.e(this.javaClass.name,"${location?.latitude}+  ${location?.longitude}")
                        val i=Intent(this,MapsActivity::class.java).putExtra("type","location").putExtra("Latitude",location?.latitude).putExtra("Longitude",location?.longitude)
                        overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                        startActivity(i)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }

                }
            }

        })


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
                    // Notify our Activity that a new location was added. Again, if this was a
                    // production app, the Activity would be listening for changes to a database
                    // with new locations, but we are simplifying things a bit to focus on just

                } else {
                    Log.e(this.javaClass.name.toString(), "Location missing in callback.")
                }
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this@LocationActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@LocationActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@LocationActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@LocationActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                enableLoc()
            }
        }
    }

    private fun getAddress(location: LatLng): String {
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address: String =
            addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city: String = addresses[0].locality
        val state: String = addresses[0].adminArea
        val country: String = addresses[0].countryName
        val postalCode: String = addresses[0].postalCode
        val knownName: String = addresses[0].featureName // Only if available else return NULL

        return address;
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
                    if ((ContextCompat.checkSelfPermission(
                            this@LocationActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ==
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        enableLoc()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    locationAlertDialog()
                }
                return
            }
        }
    }

    fun locationAlertDialog(){
        MaterialAlertDialogBuilder(this@LocationActivity,R.style.AlertDialogTheme)
            .setTitle("Location Denied")
            .setMessage("Lorem ipsum dolor ....")
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                getLocationPermission()
            })
            .setNeutralButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                finishAffinity()
            })
            .show();
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUESTLOCATION -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(this.localClassName, "OK")
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
                    getLocationCallback()
                    Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
                }
                Activity.RESULT_CANCELED -> Log.d(this.localClassName, "CANCEL")
            }
        }
    }

    private fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this@LocationActivity).build()
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

                            Toast.makeText(
                                this@LocationActivity,
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
                                status.startResolutionForResult(this@LocationActivity, REQUESTLOCATION)
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

    override fun onConnected(p0: Bundle?) {
        Toast.makeText(this, "Connected " + p0.toString(), Toast.LENGTH_SHORT).show()
        Log.e("CONNECTION", p0.toString())
    }

    override fun onConnectionSuspended(p0: Int) {

        Log.e("onConnectionSuspended", p0.toString())
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

        Log.e("onConnectionFailed ", p0.toString())
    }


    override fun onLocationChanged(location: Location) {

        Log.e(
            "onLocationChanged",
            location.latitude.toString() + ",  " + location.longitude.toString()
        )
        /*Toast.makeText(
            this,
            "Location  " + location?.longitude + ",  " + location?.latitude,
            Toast.LENGTH_SHORT
        ).show()*/
        location1 = location
    }

    override fun onProviderDisabled(provider: String) {
        enableLoc()
    }

    fun getNearLocation(view: View) {
        if (latLng != null ){
            moveToLocation()
        }else{
            Toast.makeText(this, "Set location first", Toast.LENGTH_SHORT).show()
        }
    }
    private fun moveToLocation() {
        val intent = Intent(this@LocationActivity, HomeBaseActivity::class.java).putExtra(
            "latlang",
            latLng
        )
        overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
        startActivity(intent)
        finish()
    }
}