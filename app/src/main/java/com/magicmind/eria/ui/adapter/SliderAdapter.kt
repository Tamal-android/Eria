package com.magicmind.eria.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.magicmind.eria.databinding.FragmentViewPagerBinding
import com.magicmind.eria.ui.viewModel.Slider
import com.magicmind.eria.utils.setImageFromRaw


class SliderAdapter(categories: List<Slider>):
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    var sliders: List<Slider>? = categories

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderAdapter.SliderViewHolder {
        val sliderItemBinding: FragmentViewPagerBinding = FragmentViewPagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return SliderViewHolder(sliderItemBinding)
    }

    override fun onBindViewHolder(holder: SliderAdapter.SliderViewHolder, position: Int) {

        holder.bind(sliders?.get(position%sliders!!.size));
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }
    class SliderViewHolder(sliderItemBinding: FragmentViewPagerBinding) :
        RecyclerView.ViewHolder(sliderItemBinding.root) {
        var sliderItemBinding: FragmentViewPagerBinding = sliderItemBinding
        fun bind(slider: Slider?) {
            sliderItemBinding.onBoardingImage.setImageFromRaw(slider?.getUrl()!!)
            sliderItemBinding.slider = slider
            sliderItemBinding.root.setOnClickListener { v ->
                Log.e(
                    "selected Items",
                    Gson().toJson(slider)
                )
            }
        }

    }

}
