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
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.magicmind.eria.R
import com.magicmind.eria.data.model.response.MenuList
import com.magicmind.eria.databinding.FragmentMenuListBinding
import com.magicmind.eria.ui.adapter.ImageSlideAdaptor
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

    /*private val pageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        var first = false
        var last = false
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position == 0)
            {
                first = true;
                last = false;
            }
            else if (position == 2)
            {
                first = false;
                last = true;
            }
            else
            {
                first = false;
                last = false;
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (first && state === ViewPager.SCROLL_STATE_IDLE) {
                // Jump without animation
                val fragment: Fragment = mainFragmentList.get(mainFragmentList.size() - 1)
                mainFragmentList.remove(mainFragmentList.size() - 1)
                mainFragmentList.add(0, fragment)
                mainPagerAdapter.setData(mainFragmentList)
                mainPagerAdapter.notifyDataSetChanged()
                Log.e(TAG, mainFragmentList.toString())
                mainViewPager.setCurrentItem(1, false)
            }
            if (last && state === ViewPager.SCROLL_STATE_IDLE) {
                // Jump without animation
                val fragment: Fragment = mainFragmentList.get(0)
                mainFragmentList.remove(0)
                mainFragmentList.add(fragment)
                mainPagerAdapter.setData(mainFragmentList)
                mainPagerAdapter.notifyDataSetChanged()
                Log.e(TAG, mainFragmentList.toString())
                mainViewPager.setCurrentItem(mainFragmentList.size() - 2, false)
            }
        }
    }*/

    var itemCount:Int=0
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

//        val imageSlideAdapter = ImageSlideAdaptor(baseActivity!!, 3)
//        binding.vpImageSlide.adapter = imageSlideAdapter
//        binding.vpImageSlide.setCurrentItem(binding.vpImageSlide.getChildCount() * 1000 / 2, false)
        /*binding.vpImageSlide.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                handler.removeMessages(0)

                val runnable = Runnable {
                    binding.vpImageSlide.currentItem = ++binding.vpImageSlide.currentItem
                }
                if (position < binding.vpImageSlide.adapter?.itemCount ?: 0) {
                    handler.postDelayed(runnable, 1000)
                }
            }
        })*/


        implementSlider(listOf(Slider(R.raw.on_board_img1_land)))
       // binding.vpImageSlide.registerOnPageChangeCallback(pageChangeCallback)
        var adapter: MenuListAdapter? = null

        val menuList = ArrayList<MenuList>()
        adapter = MenuListAdapter(baseActivity!!, binding, menuList)

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

    private fun implementSlider(sliders: List<Slider>) {
        val sliderAdapter = SliderAdapter( sliders,binding.vpImageSlide)
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
    override fun onDestroyView() {
        super.onDestroyView()
        baseActivity!!.setToolbarTitle("")
        baseActivity!!.showBottomNavigationBar(true)
        baseActivity?.showHeader(false)
        baseActivity?.setToolbarTitle("")
        baseActivity?.enableBackButton(false)
    }

}