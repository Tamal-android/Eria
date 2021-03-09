package com.magicmind.eria.ui.fragment

import android.content.Context
import android.os.Bundle
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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.magicmind.eria.R
import com.magicmind.eria.data.model.response.MenuList
import com.magicmind.eria.databinding.FragmentMenuListBinding
import com.magicmind.eria.ui.adapter.ImageSlideAdaptor
import com.magicmind.eria.ui.adapter.MenuListAdapter
import com.magicmind.eria.ui.base.BaseFragment
import com.magicmind.eria.ui.base.HomeBaseActivity
import com.magicmind.eria.ui.viewModel.MenuItemViewModel
import kotlinx.android.synthetic.main.fragment_menu_list.*


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAppBarLayout = ablMenuList as AppBarLayout
        val mTitleText = ctlCollapseToolbar as CollapsingToolbarLayout
        mTitleText.title = "Resturent Name";
        mTitleText.apply {
            setCollapsedTitleTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_bold))
            setExpandedTitleTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_bold))
            setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
            setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        }

        val imageSlideAdapter = ImageSlideAdaptor(baseActivity!!, 3)
        binding.vpImageSlide.adapter = imageSlideAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        baseActivity!!.setToolbarTitle("")
        baseActivity!!.showBottomNavigationBar(true)
        baseActivity?.showHeader(false)
        baseActivity?.setToolbarTitle("")
        baseActivity?.enableBackButton(false)
    }

}