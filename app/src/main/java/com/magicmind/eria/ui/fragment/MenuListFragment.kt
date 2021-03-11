package com.magicmind.eria.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.magicmind.eria.R
import com.magicmind.eria.data.model.response.MenuList
import com.magicmind.eria.data.model.response.SliderImageResponse
import com.magicmind.eria.data.network.ApiCallback
import com.magicmind.eria.databinding.FragmentMenuListBinding
import com.magicmind.eria.ui.Interface.onMenuItemAdd
import com.magicmind.eria.ui.adapter.MenuListAdapter
import com.magicmind.eria.ui.adapter.SliderAdapter
import com.magicmind.eria.ui.base.BaseFragment
import com.magicmind.eria.ui.base.HomeBaseActivity
import com.magicmind.eria.ui.viewModel.MenuItemViewModel
import com.magicmind.eria.ui.viewModel.Slider
import kotlinx.android.synthetic.main.fragment_menu_list.*
import kotlin.math.abs


class MenuListFragment : BaseFragment() {

    lateinit var binding: FragmentMenuListBinding
    private var baseActivity: HomeBaseActivity? = null
    private var currentPosition = 0
    private var menuItemViewModel: MenuItemViewModel?=null
    private val fakeSize = 0
    private val realSize = 0
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    companion object {
        fun newInstance(): MenuListFragment {
            return MenuListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = requireActivity() as HomeBaseActivity
    }

    override fun getFragmentActivityReference(activity: HomeBaseActivity) {
        this.baseActivity = activity

        //baseActivity?.setToolbarTitle("Profile")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_list, container, false)
            menuItemViewModel = ViewModelProviders.of(this).get(MenuItemViewModel::class.java)
            //lifecycleOwner = viewLifecycleOwner
            binding.viewModel = menuItemViewModel

        baseActivity?.showHeader(false)
        baseActivity?.enableBackButton(true)
        baseActivity?.showBottomNavigationBar(false)
        return binding.root
    }

    val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var origPosition: Int = 0
        val mAppBarLayout = ablMenuList as AppBarLayout
        val mTitleText = ctlCollapseToolbar as CollapsingToolbarLayout
        mTitleText.title = "Resturent Name";
        mTitleText.apply {
            setCollapsedTitleTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_bold))
            setExpandedTitleTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_bold))
            setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
            setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        }


        callSliderApi()
        var adapter: MenuListAdapter? = null

        val menuList = ArrayList<MenuList>()
        adapter = MenuListAdapter(baseActivity!!, binding, menuList,object : onMenuItemAdd {
            override fun onItemAdded(position: Boolean, count: Int?) {
                if (position){
                    showBottomSheetDialogFragment()
                }
            }
        })

        menuItemViewModel?.count?.observe(viewLifecycleOwner, Observer {
            Log.e("OBSERVER", it.toString())
            if (it > 0) {
                binding.btnAddCart.visibility = View.VISIBLE
                binding.btnAddCart.text = "${it.toString()} Item Added : GO TO CART."
            } else {
                binding.btnAddCart.visibility = View.GONE
            }
        })
        binding.btnMenuSort.setOnClickListener(View.OnClickListener {
            val wrapper: Context = ContextThemeWrapper(context, R.style.PopupMenu)
            val popup = PopupMenu(wrapper, it)//Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.item_catagory, popup.menu)
            popup.menu.add("Appetizer")
            popup.menu.add("Starter")
            popup.menu.add("Main Course")
            popup.menu.add("Desert")
            popup.show()
        })


        binding.includeLayout.rvMenuList!!.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.VERTICAL,
            false
        )
        binding.includeLayout.tvMenuTitle.text = "Menu List"

        binding.includeLayout.rvMenuList.adapter = adapter
        for (i in 10 downTo 1) {
            var movie = MenuList("", "abcd")
            menuList.add(movie)
        }
        adapter.notifyDataSetChanged()

    }
    private val runnable = Runnable {
        binding.vpImageSlide.currentItem = binding.vpImageSlide.currentItem + 1
    }

    private fun showBottomSheetDialogFragment() {
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(baseActivity?.supportFragmentManager!!, bottomSheetFragment.tag)
    }

    private fun implementSlider(sliders: List<Slider>) {
        val sliderAdapter = SliderAdapter( sliders)
        binding.vpImageSlide.adapter = sliderAdapter
        binding.vpImageSlide.clipToPadding = false
        binding.vpImageSlide.clipChildren = false
        binding.vpImageSlide.offscreenPageLimit = 3
        binding.vpImageSlide.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.vpImageSlide.setCurrentItem(0, false)
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(10))
        compositePageTransformer.addTransformer { page: View, position: Float ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.vpImageSlide.setPageTransformer(compositePageTransformer)
        binding.vpImageSlide.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 4000)
            }
        })
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


    override fun onDestroyView() {
        super.onDestroyView()
        baseActivity!!.setToolbarTitle("")
        baseActivity!!.showBottomNavigationBar(true)
        baseActivity?.showHeader(false)
        baseActivity?.setToolbarTitle("")
        baseActivity?.enableBackButton(false)
    }

}