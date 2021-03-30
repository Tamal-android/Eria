package com.magicmind.eria.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.magicmind.eria.R
import com.magicmind.eria.data.model.response.ResturentResponse
import com.magicmind.eria.data.model.response.SliderImageResponse
import com.magicmind.eria.data.model.response.TopPicks
import com.magicmind.eria.data.network.ApiCallback
import com.magicmind.eria.databinding.FragmentDashboardBinding
import com.magicmind.eria.ui.activity.MapsActivity
import com.magicmind.eria.ui.adapter.SliderAdapter
import com.magicmind.eria.ui.adapter.TopBrandsAdapter
import com.magicmind.eria.ui.adapter.TopFoodsAdapter
import com.magicmind.eria.ui.adapter.TopPicksAdapter
import com.magicmind.eria.ui.base.BaseFragment
import com.magicmind.eria.ui.base.HomeBaseActivity
import com.magicmind.eria.ui.viewModel.Slider
import kotlin.math.abs


class DashboardFragment : BaseFragment() {

    private lateinit var binding: FragmentDashboardBinding
    private var baseActivity: HomeBaseActivity? = null

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = requireActivity() as HomeBaseActivity
    }

    override fun getFragmentActivityReference(activity: HomeBaseActivity) {

        //baseActivity?.showHeader(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    val handler = Handler(Looper.getMainLooper())
    private fun implementSlider(sliders: List<Slider>) {
        val sliderAdapter = SliderAdapter( sliders)
        binding.vpBannerSlide.adapter = sliderAdapter
        binding.vpBannerSlide.clipToPadding = false
        binding.vpBannerSlide.clipChildren = false
        binding.vpBannerSlide.offscreenPageLimit = 3
        binding.vpBannerSlide.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.vpBannerSlide.setCurrentItem(0, false)
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(10))
        compositePageTransformer.addTransformer { page: View, position: Float ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.vpBannerSlide.setPageTransformer(compositePageTransformer)
        binding.vpBannerSlide.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 4000)
            }
        })
    }
    private val runnable = Runnable {
        binding.vpBannerSlide.currentItem = binding.vpBannerSlide.currentItem + 1
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity?.showHeader(false)

        if (baseActivity!!.latLng!=null)
            binding.tvTitle.text = baseActivity!!.getAddress(baseActivity!!.latLng!!,null)
        if (baseActivity!!.location1!=null)
            binding.tvTitle.text = baseActivity!!.getAddress(null,baseActivity!!.location1!!)
        binding.tvTitle.isSelected = true;
        binding.tvTitle.ellipsize = TextUtils.TruncateAt.END;
        binding.tvTitle.setHorizontallyScrolling(true);
        binding.tvTitle.isSingleLine = true;
        binding.tvTitle.setLines(1);
        binding.ivCartImage.setOnClickListener {
            baseActivity?.updateNavigationBarState(R.id.navigation_cart)
            baseActivity?.showHeader(true)
            baseActivity?.showBottomNavigationBar(true)
            baseActivity?.changeFragment(
                CartFragment.newInstance(),
                isAddToBackStack = false
            )
        }
        binding.ivUserImage.setOnClickListener {
            baseActivity?.updateNavigationBarState(R.id.navigation_profile)
            baseActivity?.showHeader(true)
            baseActivity?.showBottomNavigationBar(true)
            baseActivity?.changeFragment(
                ProfileFragment.newInstance(),
                isAddToBackStack = false
            )
        }

        binding.tvTitle.setOnClickListener(View.OnClickListener {
            if (!baseActivity?.locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                baseActivity?.enableLoc()
            }else{
                moveToLocation()
            }
        })

        binding.svSearch.setOnClickListener(View.OnClickListener {
            binding.svSearch.isIconified = false
        })

        callResturentApi()
        callSliderApi()
        var adapter: TopBrandsAdapter? = null

        adapter = TopBrandsAdapter(baseActivity!!)
        binding.recyclerviewBrand.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerviewBrand.adapter = adapter

        var adapter1: TopFoodsAdapter? = null

        adapter1 = TopFoodsAdapter(baseActivity!!)
        binding.recyclerviewFood.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerviewFood.adapter = adapter1

        var adapter2: TopPicksAdapter? = null

        val toppicksList = ArrayList<TopPicks>()
        adapter2 = TopPicksAdapter(baseActivity!!, toppicksList)
        binding.recyclerviewTop.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerviewTop.adapter = adapter2
        for (i in 10 downTo 1) {
            var movie = TopPicks("", "abcd")
            toppicksList.add(movie)
        }
        adapter2.notifyDataSetChanged()
    }

    private fun moveToLocation() {
        if (ActivityCompat.checkSelfPermission(
                baseActivity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                baseActivity!!,
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
        baseActivity?.fusedLocationProviderClient!!.lastLocation.addOnSuccessListener { location: Location? ->
            if (location==null){
                baseActivity?.enableLoc()
            }else {

                try {
                    Log.e(this.javaClass.name, "${location?.latitude}+  ${location?.longitude}")
                    val i =
                        Intent(baseActivity, MapsActivity::class.java).putExtra("type", "location")
                            .putExtra("Latitude", location?.latitude)
                            .putExtra("Longitude", location?.longitude)
                    baseActivity?.overridePendingTransition(
                        R.anim.popup_in_anim,
                        R.anim.popup_out_anim
                    )
                    startActivity(i)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun callSliderApi() {
       // baseActivity?.showProgress(getString(R.string.txt_progress_loading))
        val sliderApiCall = baseActivity?.getWebService()!!.callBannerSliderApi()
        sliderApiCall!!.enqueue(object : ApiCallback<SliderImageResponse>() {
            override fun onFinish() {
                baseActivity?.hideProgress()
            }

            override fun onSuccess(model: SliderImageResponse?) {

                Log.e("SLIDER",model?.status!!.toString())
                if (!model.status){
                    Log.e("SLIDER",model.data?.banners?.get(0)?.image!!)
                    val listSlide= mutableListOf<Slider>()
                    for (i in model.data?.banners!!.indices){
                        listSlide.add(Slider(model.data?.image?.banner_image_path+model.data?.banners?.get(i)?.image!!))
                    }
                    implementSlider(listSlide!!)
                }
            }

            override fun onFailure(code: Int, msg: String?) {
                Log.e("Logout", "$msg :$code")
                if (code == 403) {
                    baseActivity?.showToast("User not registered")
                } else
                    baseActivity?.showToast(msg!!)
            }

            override fun onThrowable(t: Throwable?) {

                Log.e("SLIDER",t.toString())
                baseActivity?.showToast(getString(R.string.error_parse))
            }

        })
    }

    private fun callResturentApi() {
       // baseActivity?.showProgress(getString(R.string.txt_progress_loading))
        val resturentApiCall = baseActivity?.getWebService()!!.callResturentApi()
        resturentApiCall!!.enqueue(object : ApiCallback<ResturentResponse>() {
            override fun onFinish() {
                baseActivity?.hideProgress()
            }

            override fun onSuccess(model: ResturentResponse?) {

                Log.e("SLIDER",model?.status!!.toString())
                /*if (!model.status){
                    Log.e("SLIDER",model.data?.banners?.get(0)?.image!!)
                    val listSlide= mutableListOf<Slider>()
                    for (i in model.data?.banners!!.indices){
                        listSlide.add(Slider(model.data?.image?.banner_image_path+model.data?.banners?.get(i)?.image!!))
                    }
                    implementSlider(listSlide!!)
                }*/
            }

            override fun onFailure(code: Int, msg: String?) {
                Log.e("Logout", "$msg :$code")
                if (code == 403) {
                    baseActivity?.showToast("User not registered")
                } else
                    baseActivity?.showToast(msg!!)
            }

            override fun onThrowable(t: Throwable?) {

                Log.e("SLIDER",t.toString())
                baseActivity?.showToast(getString(R.string.error_parse))
            }

        })
    }


}