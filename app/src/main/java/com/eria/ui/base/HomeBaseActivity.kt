package com.eria.ui.base
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.eria.R
import com.eria.databinding.ActivityHomeBaseBinding
import com.eria.databinding.FragmentProfileBinding
import com.eria.ui.fragment.CartFragment
import com.eria.ui.fragment.DashboardFragment
import com.eria.ui.fragment.ProfileFragment
import com.eria.ui.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.ConcurrentHashMap


class HomeBaseActivity : BaseActivity(), View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener{
    private val mapOfAddedFragments: ConcurrentHashMap<String, Fragment> = ConcurrentHashMap()

    private val tv_toolbar_title: AppCompatTextView by bind(R.id.tv_toolbar_text)
    private val toolbar: Toolbar by bind(R.id.toolbar)
    private val bottomNavigationView: BottomNavigationView by bind(R.id.bottomNavigationView)

    private lateinit var binding: ActivityHomeBaseBinding

    //To Handle Back press event
    private var back_pressed: Long = 0
    private var dashboardFragmentInstance: DashboardFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_base)
        setSupportActionBar(toolbar)

        initView()
        initClickListener()

        changeFragment(DashboardFragment.newInstance())

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
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


    override fun onBackPressed() {
        val fm = supportFragmentManager
        val entryCount = fm.backStackEntryCount
        if (entryCount > 1) {
            popFragment()
        } else {
            if (back_pressed + 1500 > System.currentTimeMillis()) {
                finish()
                super.onBackPressed()
            } else {
                showToast(getString(R.string.press_again_to_exit))
            }
            back_pressed = System.currentTimeMillis()
        }
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
    private fun changeFragment(fragment: Fragment) {
        addFragmentToBottomNav(R.id.fl_container, fragment)
    }
    fun popFragment() {
        supportFragmentManager.popBackStack()
    }
    @SuppressLint("ResourceType")
    protected fun addFragmentToBottomNav(@LayoutRes fragmentContainer: Int, fragment: Fragment) {
        if (fragment != null) {
            val fragmentTag = fragment.javaClass.name

            if (!mapOfAddedFragments.containsKey(fragmentTag))
                mapOfAddedFragments[fragmentTag] = fragment

            val fragmentTransaction = supportFragmentManager.beginTransaction()

            val fragmentToAdd = mapOfAddedFragments[fragmentTag]

            if (fragmentToAdd != null) {
                if (fragmentToAdd.isAdded)
                    fragmentTransaction.show(fragmentToAdd)
                else
                    fragmentTransaction.add(fragmentContainer, fragment, fragmentTag)
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }

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

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

            R.id.navigation_home -> {
           showHeader(false)
                changeFragment(DashboardFragment.newInstance())
            }


            R.id.navigation_profile -> {
                showHeader(true)
                changeFragment(ProfileFragment.newInstance())
            }


            R.id.navigation_cart -> {
                showHeader(true)
                changeFragment(CartFragment.newInstance())
            }
            R.id.navigation_search -> {
                showHeader(false)
                changeFragment(SearchFragment.newInstance())
            }

        }
        return true
    }



    fun showHeader(isShow: Boolean) {
        if (isShow)
            toolbar.visibility = View.VISIBLE
        else
            toolbar.visibility = View.GONE
    }



}