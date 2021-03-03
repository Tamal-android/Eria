package com.magicmind.eria.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.magicmind.eria.R
import com.magicmind.eria.databinding.ActivityAddressBinding
import com.magicmind.eria.db_dao.AddressDao
import com.magicmind.eria.ui.base.BaseActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.address_cart_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddressActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks, LocationListener {
    private lateinit var binding: ActivityAddressBinding
    private lateinit var addressDao: AddressDao

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address)

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

    }

    override fun onStart() {
        super.onStart()
        if (db != null)
            addressDao = db!!.addressDao()
        loadAddress()
    }

    private fun loadAddress() {
        llAddressContainer!!.removeAllViews()
        GlobalScope.launch(Dispatchers.Main) {
            for (i in addressDao.getAll().indices) {
                val inflater =
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val rowView: View = inflater.inflate(R.layout.address_cart_view, null)
                rowView.tag = i
                rowView.tvTag.text = addressDao.getAll()[i].Tag
                rowView.tvAddress.text = addressDao.getAll()[i].Address
                rowView.tvAddress.tag = addressDao.getAll()[i].addressid
                rowView.ivMore.setOnClickListener(View.OnClickListener {
                    //Creating the instance of PopupMenu
                    val popup = PopupMenu(this@AddressActivity, it)
                    //Inflating the Popup using xml file
                    popup.menuInflater.inflate(R.menu.address_popup, popup.menu)
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener { item ->
                        if (item.itemId == R.id.delete) {
                            GlobalScope.launch(Dispatchers.IO) {
                                addressDao.deleteByUserId(rowView.tvAddress.tag as Int)
                            }
                            llAddressContainer!!.removeViewAt(
                                llAddressContainer.indexOfChild(
                                    rowView
                                )
                            )
                            Toast.makeText(
                                this@AddressActivity,
                                "You Delete : " + rowView.tvAddress.tag.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        true
                    }
                    popup.show()
                })
                llAddressContainer!!.addView(rowView)
                //  val myView: View = layoutInflater.inflate(R.layout.address_cart_view, null)
            }

        }
    }


    fun addAddress(view: View) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableLoc()
        } else {
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
            }
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                try {
                    Log.e(this.javaClass.name, "${location?.latitude}+  ${location?.longitude}")
                    startActivity(
                        Intent(this, MapsActivity::class.java).putExtra("type", "address")
                            .putExtra("Latitude", location?.latitude)
                            .putExtra("Longitude", location?.longitude)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        /* val i = Intent(this@AddressActivity, LocationActivity::class.java)
         i.putExtra("")
         overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
         startActivity(i)*/

        /*val thread = Thread {
          //  addressDao.insertAll(Address(et1.text.toString(), et2.text.toString()))
        }
        thread.start()*/
    }

    private fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this@AddressActivity)
                .addOnConnectionFailedListener(this@AddressActivity).build()
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
                                this@AddressActivity,
                                "CONNECTED ACD",
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
                                    this@AddressActivity,
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
                this@AddressActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AddressActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@AddressActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@AddressActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                enableLoc()
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onLocationChanged(location: Location) {
        location1 = location
    }

}