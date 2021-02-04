package com.eria.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eria.R
import com.eria.data.model.response.MenuList
import com.eria.data.model.response.TopPicks
import com.eria.databinding.FragmentMenuListBinding
import com.eria.ui.Interface.onMenuItemAdd
import com.eria.ui.adapter.MenuListAdapter
import com.eria.ui.base.HomeBaseActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.fragment_menu_list.*


class MenuListFragment : Fragment() {

    private lateinit var binding: FragmentMenuListBinding
    private var baseActivity: HomeBaseActivity? = null

    companion object {
        fun newInstance(): MenuListFragment {
            return MenuListFragment()
        }
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_list, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity?.showHeader(false)
        baseActivity?.showBottomNavigationBar(false)

        val mAppBarLayout = ablMenuList as AppBarLayout
        val mTitleText = tv_toolbar_title as AppCompatTextView
        mTitleText.text = "";
        mAppBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    mTitleText.text = "Resturent Name";
                   // showOption(R.id.action_info)
                } else if (isShow) {
                    isShow = false
                    mTitleText.text = "";
                    //hideOption(R.id.action_info)
                }
            }
        })

        var adapter: MenuListAdapter? = null

        val menuList = ArrayList<MenuList>()
        adapter = MenuListAdapter(baseActivity!!,menuList,object :onMenuItemAdd{
            override fun onItemAdded(position: Boolean) {
                if (position){
                    binding.btnAddCart.visibility=View.VISIBLE
                }else{
                    binding.btnAddCart.visibility=View.GONE
                }

            }
        })
        binding.includeLayout.rvMenuList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,
            false)
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
    }

}