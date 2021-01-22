package com.eria.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dc.dlocation.listener.LocationListener
import com.eria.R
import com.eria.databinding.FragmentLocationRequestBinding
import com.eria.ui.base.HomeBaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationRequestFragment : Fragment(), View.OnClickListener, LocationListener {

    private lateinit var binding: FragmentLocationRequestBinding
    private var baseActivity: HomeBaseActivity? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    companion object {
        fun newInstance(): LocationRequestFragment {
            return LocationRequestFragment()
        }

        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = requireActivity() as HomeBaseActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_request, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity?.showHeader(true)
        baseActivity?.setToolbarTitle("Set Location")
        binding.btnMyLocation.setOnClickListener(this)
        binding.btnCustomLocation.setOnClickListener(this)

        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        baseActivity!!.initLocation(this)
        baseActivity!!.getLocation()
        //showToast(homeBaseActivity.getLocation().toString())
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful && task.result != null) {

                showMessage(task.result!!.latitude.toString()+"  "+task.result!!.longitude.toString())
                //lastLocation = task.result
              //  latitudeText!!.text = latitudeLabel + ": " + (lastLocation)!!.latitude
              //  longitudeText!!.text = longitudeLabel + ": " + (lastLocation)!!.longitude
            }
            else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                showMessage("No location detected. Make sure location is enabled on the device.",)
            }
        }
    }

    private fun showMessage(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_LONG).show()
    }
    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(requireContext(), mainTextStringId, Toast.LENGTH_LONG).show()
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btn_MyLocation-> {

                getCurrentLocation()
            }
            R.id.btn_CustomLocation-> {

            }
        }
    }


    override fun onProcessTypeChanged(processType: Int) {
    }

    override fun onLocationChanged(location: Location?) {
        print("LOCATION   "+location?.latitude.toString()+"  "+location?.longitude.toString())
        showMessage("LOCATION   "+location?.latitude.toString()+"  "+location?.longitude.toString())
    }

    override fun onLocationFailed(type: Int) {
    }

    override fun onPermissionGranted(alreadyHadPermission: Boolean) {
        //showMessage("GRANTED")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
        showMessage("ENABLED")
    }

    override fun onProviderDisabled(provider: String?) {
    }
}