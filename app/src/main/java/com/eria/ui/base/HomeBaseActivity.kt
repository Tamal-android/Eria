package com.eria.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.eria.R
import com.eria.databinding.ActivityHomeBaseBinding
import com.eria.ui.dialog.CircularProgressBar
import com.eria.ui.fragment.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


class HomeBaseActivity : BaseActivity(), View.OnClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    private val mapOfAddedFragments: ConcurrentHashMap<String, Fragment> = ConcurrentHashMap()

    private val tv_toolbar_title: AppCompatTextView by bind(R.id.tv_toolbar_text)
    private val toolbar: Toolbar by bind(R.id.toolbar)
    private val bottomNavigationView: BottomNavigationView by bind(R.id.bottomNavigationView)

    private lateinit var binding: ActivityHomeBaseBinding

    //To Handle Back press event
    private var back_pressed: Long = 0
    private var dashboardFragmentInstance: DashboardFragment? = null
    var latLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_base)
        setSupportActionBar(toolbar)
        if (intent.getParcelableExtra<LatLng>("latlang") != null) {
            latLng = intent.getParcelableExtra<LatLng>("latlang")

            binding.tvToolbarText.text = getAddress(latLng!!)
        }
        initView()
        initClickListener()
        showHeader(false)
        showBottomNavigationBar(true)
        changeFragment(
            DashboardFragment.newInstance(),
            isAddToBackStack = false
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) window.statusBarColor = Color.TRANSPARENT

    }

    override fun onResume() {
        super.onResume()
        if (getLocationManager() != null) {
            if (getLocationManager()!!.isWaitingForLocation
                && !getLocationManager()!!.isAnyDialogShowing
            ) {
                // showProgress("Please Wait. Getting Updated Location")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        hideProgress()
    }

    private fun initView() {

    }

    private fun initClickListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

    }

    fun showKeyboard() {
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun closeKeyboard() {
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
    override fun onBackPressed() {
        //showHeader(true)
        //showBottomNavigationBar(true)
        val fm = supportFragmentManager
        val entryCount = fm.backStackEntryCount
        if (entryCount < 1) {
            showBottomNavigationBar(true)
            if (back_pressed + 1500 > System.currentTimeMillis()) {
                finish()
                super.onBackPressed()
            } else {
                showToast(getString(R.string.press_again_to_exit))
            }
            back_pressed = System.currentTimeMillis()
        } else {
            popFragment()
        }
    }

    fun getAddress(location: LatLng): String {
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address: String =
            addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val area: String = addresses[0].subLocality
        val city: String = addresses[0].locality
        val state: String = addresses[0].adminArea
        val country: String = addresses[0].countryName
        val postalCode: String = addresses[0].postalCode
        val knownName: String = addresses[0].featureName // Only if available else return NULL

        return address;
    }

    fun setToolbarTitle(toolbarText: String) {
        if (!TextUtils.isEmpty(toolbarText)) {
            tv_toolbar_title.visibility = View.VISIBLE
            tv_toolbar_title.text = toolbarText
        } else {
            tv_toolbar_title.visibility = View.GONE
        }
    }

    @SuppressLint("ResourceType")
    fun changeFragment1(
        fragment: Fragment,
        isAddToBackStack: Boolean,
        isFragmentReplaced: Boolean
    ) {
        if (fragment == DashboardFragment.newInstance()) {
            showHeader(false)
            showBottomNavigationBar(true)
        }
        addFragmentToBottomNav(R.id.fl_container, fragment, isAddToBackStack, isFragmentReplaced)
    }

    fun changeFragment(fragment: Fragment, isAddToBackStack: Boolean) {
        val fragment_name = fragment.javaClass.simpleName
        val mFragment = supportFragmentManager.findFragmentByTag(fragment_name)
        if (mFragment == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fl_container, fragment, fragment_name)
            if (isAddToBackStack)
                fragmentTransaction.addToBackStack(fragment_name)
            fragmentTransaction.commit()
        }
    }

    fun popFragment() {
        supportFragmentManager.popBackStack()
    }


    @SuppressLint("ResourceType")
    protected fun addFragmentToBottomNav(
        @LayoutRes fragmentContainer: Int,
        fragment: Fragment,
        isAddToBackStack: Boolean,
        isFragmentReplaced: Boolean
    ) {
        if (fragment != null) {
            val fragmentTag = fragment.javaClass.name

            if (!mapOfAddedFragments.containsKey(fragmentTag))
                mapOfAddedFragments[fragmentTag] = fragment

            val fragmentTransaction = supportFragmentManager.beginTransaction()

            val fragmentToAdd = mapOfAddedFragments[fragmentTag]

            if (fragmentToAdd != null) {
                if (fragmentToAdd.isAdded)
                    fragmentTransaction.show(fragmentToAdd)
                else {
                    if (isFragmentReplaced)
                        fragmentTransaction.replace(fragmentContainer, fragment, fragmentTag)
                    else
                        fragmentTransaction.add(fragmentContainer, fragment, fragmentTag)
                }
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }

            if (isAddToBackStack)
                fragmentTransaction.addToBackStack(fragmentTag)
            for ((key, fragmentTemp) in mapOfAddedFragments) {
                if (key != fragmentTag) {
                    if (fragmentTemp != null)
                        if (fragmentTemp.isAdded)
                            fragmentTransaction.hide(fragmentTemp)
                }
            }

            fragmentTransaction.commit()
        }
    }

    override fun onClick(v: View?) {
    }

    fun showCustomDialog(titleText: String, type: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_layout)
        val tvDialogTitle = dialog.findViewById<AppCompatTextView>(R.id.tvDialogTitle)
        val rbReviewRate = dialog.findViewById<AppCompatRatingBar>(R.id.rbReviewRate)
        val custom_progressBar = dialog.findViewById<CircularProgressBar>(R.id.custom_progressBar)
        val tvProgress = dialog.findViewById<AppCompatTextView>(R.id.tvProgress)
        val et_desc_video = dialog.findViewById<AppCompatEditText>(R.id.et_desc_video)
        val btn_cancel = dialog.findViewById<AppCompatButton>(R.id.btn_cancel)
        tvDialogTitle.text = titleText
        when (type) {
            "order" -> {
                rbReviewRate.visibility = View.GONE
                et_desc_video.visibility = View.GONE
                tvProgress.visibility = View.VISIBLE
                custom_progressBar.visibility = View.VISIBLE
                orderProgress(tvProgress, custom_progressBar, dialog)
            }
        }

        btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private val FORMAT = "%02d"
    private fun orderProgress(
        progressText: AppCompatTextView,
        customProgressBar: CircularProgressBar,
        dialog: Dialog
    ) {
        object : CountDownTimer(30000, 1000) {
            var numberOfSeconds: Int = 30000 / 1000 // Ex : 20000/1000 = 20

            var factor = 100 / numberOfSeconds

            override fun onTick(millisUntilFinished: Long) {

                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                val progressPercentage: Int = (numberOfSeconds - secondsRemaining) * factor

                customProgressBar.progress = progressPercentage.toFloat()
                progressText.text = String.format(
                    FORMAT,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                ) + "\nSec";
            }

            override fun onFinish() {
                // binding.tvResendOtp.isEnabled=true
                progressText.text = "Finished"
                customProgressBar.progress = 100f
            }
        }.start()
    }
    fun updateNavigationBarState(actionId: Int) {
        val item: MenuItem = bottomNavigationView.menu.findItem(actionId)
        item.isChecked = true
    }
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

            R.id.navigation_home -> {
                showHeader(false)
                showBottomNavigationBar(true)
                changeFragment(
                    DashboardFragment.newInstance(),
                    isAddToBackStack = false
                )
            }


            R.id.navigation_profile -> {
                showHeader(true)
                showBottomNavigationBar(true)
                changeFragment(
                    ProfileFragment.newInstance(),
                    isAddToBackStack = false
                )
            }


            R.id.navigation_cart -> {
                showHeader(true)
                showBottomNavigationBar(true)
                changeFragment(
                    CartFragment.newInstance(),
                    isAddToBackStack = false
                )
            }
            R.id.navigation_search -> {
                showHeader(false)
                showBottomNavigationBar(true)
                changeFragment(
                    SearchFragment.newInstance(),
                    isAddToBackStack = false
                )
            }

        }
        return true
    }


    fun showBottomNavigationBar(isShow: Boolean) {

        if (isShow)
            bottomNavigationView.visibility = View.VISIBLE
        else
            bottomNavigationView.visibility = View.GONE
    }

    fun showHeader(isShow: Boolean) {
        if (isShow)
            toolbar.visibility = View.VISIBLE
        else
            toolbar.visibility = View.GONE
    }

    fun enableBackButton(enable: Boolean) {
        if (enable) {
            toolbar.navigationIcon =
                ContextCompat.getDrawable(this, R.drawable.ic_arrow_left)
            toolbar.setNavigationOnClickListener { onBackPressed() }
        } else {
            toolbar.navigationIcon = null
        }
    }


}