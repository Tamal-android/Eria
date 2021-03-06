package com.magicmind.eria.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.magicmind.eria.R

class TutorialAdapter(
    private val mContext: Context,
    private val bannerDataList: IntArray,
    private val bannerTitleDataList: IntArray,
    private val bannerTextDataList: IntArray) : PagerAdapter() {
    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val itemView =
            inflater.inflate(R.layout.row_tutorial, collection, false)
        val imageView = itemView.findViewById<ImageView>(R.id.banner_image)
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val tvText = itemView.findViewById<TextView>(R.id.tv_text)


        val bannerData = bannerDataList[position]
        val bannerTitle = bannerTitleDataList[position]
        val bannerText = bannerTextDataList[position]
        imageView.setImageResource(bannerData)
        tvText.setText(bannerText)
        tvTitle.setText(bannerTitle)
        //Picasso.with(this.mContext).load(bannerData.getImage()).fit().into(imageView);
        collection.addView(itemView)
        return itemView
    }

    override fun destroyItem(
        collection: ViewGroup,
        position: Int,
        view: Any
    ) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return bannerDataList.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

}