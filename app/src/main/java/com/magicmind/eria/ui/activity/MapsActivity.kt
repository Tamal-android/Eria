package com.magicmind.eria.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.magicmind.eria.R
import com.magicmind.eria.databinding.ActivityMapsBinding
import com.magicmind.eria.db_dao.AddressDao
import com.magicmind.eria.ui.base.BaseActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.map_location_bottom_sheet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


open class MapsActivity : BaseActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var map: GoogleMap

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var type: String? = ""
    private var latLng: LatLng? = null
    private var marker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest
    private var onCameraIdleListener: OnCameraIdleListener? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    // lateinit var mapBottomSheetDialogFragment:MapBottomSheetDialogFragment

    private lateinit var binding: ActivityMapsBinding

    private lateinit var addressDao: AddressDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)

        latitude = intent.getDoubleExtra("Latitude", 0.0)
        longitude = intent.getDoubleExtra("Longitude", 0.0)
        type = intent.getStringExtra("type")
        //address = intent.getStringExtra("Address")

        var apiKey = resources.getString(R.string.google_maps_key)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        setLayoutType()
        // Create a new Places client instance.

        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this)

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        );
        // autocompleteFragment.setOnPlaceSelectedListener(this)
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.name + ", " + place.latLng?.latitude)
                if (marker != null && marker!!.isVisible)
                    marker?.remove()
                marker = map.addMarker(
                    MarkerOptions()
                        .position(place.latLng!!)
                        .title(getAddress(place.latLng!!))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_))
                        .snippet("Latitude: " + place.latLng!!.latitude + "; Longitude: " + place.latLng!!.longitude)

                )

                // getAddress(place.latLng!!)
                val handler = Handler(Looper.myLooper() ?: mainLooper)
                handler.postDelayed(Runnable {
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            place.latLng!!,
                            18f
                        )
                    )
                }, 1000)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: $status")
            }
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        configureCameraIdle()
        etTagOther.setOnClickListener(View.OnClickListener {
        })
        etTagOther.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= etTagOther.right - etTagOther.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        // your action here

                        removeAddressTagBtnColor()

                        btnTagHome.visibility = View.VISIBLE
                        btnTagWork.visibility = View.VISIBLE
                        btnTagHotel.visibility = View.VISIBLE
                        btnTagOther.visibility = View.VISIBLE
                        if (etTagOther.text.isNotEmpty()) {
                            getTagItem = etTagOther.text.toString()
                        }
                        return true
                    }
                }
                return false
            }
        })

    }

    fun removeAddressTagBtnColor() {
        etTagOther.visibility = View.GONE
        btnTagHome.setBackgroundResource(R.drawable.bg_white_background)
        btnTagWork.setBackgroundResource(R.drawable.bg_white_background)
        btnTagHotel.setBackgroundResource(R.drawable.bg_white_background)
        btnTagOther.setBackgroundResource(R.drawable.bg_white_background)
    }

    override fun onStart() {
        super.onStart()
        if (db != null)
            addressDao = db!!.addressDao()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private fun setLayoutType() {
        if (type == "Location") {
            btn_set.text = "Set"
            llAddressContainer.visibility = View.GONE
        } else {

            btn_set.text = "Set and Proceed"
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Add a marker in Sydney and move the camera
        val currentLoc = LatLng(latitude, longitude)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            buildGoogleApiClient()
            map.isMyLocationEnabled = true
            marker = map.addMarker(
                MarkerOptions().position(currentLoc).title(getAddress(currentLoc))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_))
            )
            // map.moveCamera(CameraUpdateFactory.newLatLng( currentLoc))
            val handler = Handler(Looper.myLooper() ?: mainLooper)
            handler.postDelayed(Runnable {
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLoc,
                        18f
                    )
                )
            }, 1000)
        }

        map.setOnCameraIdleListener(onCameraIdleListener);

        map.setOnMapLongClickListener(OnMapLongClickListener { latLng ->
            marker?.remove()
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getAddress(latLng))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_))
                //.snippet("Latitude: " + latLng.latitude + "; Longitude: " + latLng.longitude)
            )
        })
    }

    private fun configureCameraIdle(): LatLng? {
        // var latLng: LatLng? = null
        onCameraIdleListener = OnCameraIdleListener {
            latLng = map.cameraPosition.target

            marker?.remove()

            Log.e(
                "MAP LOCATIOM 123",
                "Latitude: ${latLng!!.latitude}  ; Longitude:  + ${latLng!!.longitude}"
            )
            val geocoder = Geocoder(this@MapsActivity)
            try {
                val addressList = geocoder.getFromLocation(latLng!!.latitude, latLng!!.longitude, 1)
                if (addressList != null && addressList.size > 0) {
                    val locality = addressList[0].getAddressLine(0)
                    val country = addressList[0].countryName
                    val address = addressList[0].subLocality

                    Log.e(
                        "MAP LOCATIOM",
                        "Latitude: ${latLng!!.latitude}  ; Longitude:  + ${latLng!!.longitude}  + $locality"
                    )

                    marker = map.addMarker(
                        MarkerOptions()
                            .position(latLng!!)
                            .title(locality)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_))
                    )
                    marker!!.showInfoWindow()
                    if (address != null && address.isNotEmpty())
                        tvTitle.text = address

                    if (locality != null && locality.isNotEmpty()) {
                        tvSubtitle.text = locality
                        tvSubtitle.tag = latLng
                    }
                    expandCollapseSheet(true)
                    //mapBottomSheetDialogFragment.setText(locality)
                    //.snippet("Latitude: " + latLng!!.latitude + "; Longitude: " + latLng!!.longitude))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return latLng
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    private fun getAddress(location: LatLng): String {
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())

        latLng = location
        addresses = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            2
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        var address = ""
        if (addresses.isNotEmpty()) {
            address =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            val city: String = addresses[0].locality
            val state: String = addresses[0].adminArea
            val country: String = addresses[0].countryName
            val postalCode: String = addresses[0].postalCode
            val knownName: String = addresses[0].featureName // Only if available else return NULL
            if (address != null && address.isNotEmpty())
                tvSubtitle.text = address
            if (city.isNotEmpty())
                tvTitle.text = city

        }
        Log.e("LOCATION ", address)
        return address;
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Change the map type based on the user's selection.
            R.id.normal_map -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                return true
            }
            R.id.hybrid_map -> {
                map.mapType = GoogleMap.MAP_TYPE_HYBRID
                return true
            }
            R.id.satellite_map -> {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }
            R.id.terrain_map -> {
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }


    var getTagItem: String = ""
    fun tagHome(view: View) {
        removeAddressTagBtnColor()
        btnTagHome.setBackgroundResource(R.drawable.bg_red_border)
        getTagItem = "Home"
    }

    fun tagWork(view: View) {
        removeAddressTagBtnColor()
        btnTagWork.setBackgroundResource(R.drawable.bg_red_border)
        getTagItem = "Work"
    }

    fun tagHotel(view: View) {
        removeAddressTagBtnColor()
        btnTagHotel.setBackgroundResource(R.drawable.bg_red_border)
        getTagItem = "Hotel"
    }

    fun tagOther(view: View) {
        removeAddressTagBtnColor()
        btnTagHome.visibility = View.GONE
        btnTagWork.visibility = View.GONE
        btnTagHotel.visibility = View.GONE
        btnTagOther.setBackgroundResource(R.drawable.bg_red_border)
        etTagOther.visibility = View.VISIBLE
        getTagItem = "Others"
    }

    fun setLocation(view: View) {
        if (tvSubtitle.text.isNotEmpty() && latLng != null) {
            if (type == "location") {
                overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                startActivity(
                    Intent(this, LocationActivity::class.java).putExtra(
                        "latlang",
                        latLng
                    )
                )
            } else if (type == "address") {

                if (llAddressContainer.visibility == View.GONE) {

                    llAddressContainer.visibility = View.VISIBLE
                } else {
                    if (etAddress.text.toString().isNotEmpty()) {

                        if (getTagItem.isNotEmpty()) {
                            GlobalScope.launch(Dispatchers.IO) {
                                addressDao.insertAll(
                                    com.magicmind.eria.db_dao.model.Address(
                                        etAddress.text.toString(),
                                        etFloor.text.toString(),
                                        getTagItem,
                                        latLng!!.latitude.toString(),
                                        latLng!!.longitude.toString()
                                    )
                                )
                            }
                            val i = Intent(this@MapsActivity, AddressActivity::class.java)
                            i.putExtra("latlang", latLng)
                            overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                            startActivity(i)
                        }else{
                            Toast.makeText(this, "Set Tag for this location", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        etAddress.error = "No Address is set"
                    }
                }


            }

        }
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun expandCollapseSheet(isExpanded: Boolean) {
        if (isExpanded) {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                // persistentBtn.text = "Close Bottom Sheet"
            }
        } else {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                // persistentBtn.text = "Close Bottom Sheet"
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                // persistentBtn.text = "Show Bottom Sheet"
            }
        }

    }


}