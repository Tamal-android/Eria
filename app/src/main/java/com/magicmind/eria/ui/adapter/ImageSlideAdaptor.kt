package com.magicmind.eria.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.magicmind.eria.ui.base.HomeBaseActivity
import com.magicmind.eria.ui.fragment.ViewPagerFragment

class ImageSlideAdaptor(activity: HomeBaseActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return ViewPagerFragment.newInstance(position)
    }


}