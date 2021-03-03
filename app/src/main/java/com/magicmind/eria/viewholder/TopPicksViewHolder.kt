package com.magicmind.eria.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.magicmind.eria.R
import com.magicmind.eria.data.model.response.TopPicks

class TopPicksViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    var iv_gallery = itemView.findViewById(R.id.banner_image) as AppCompatImageView

    fun bindItems(galleryDataItem: TopPicks) {
        var imgUrl = galleryDataItem.imagePath
        Glide.with(itemView.context)
            .asBitmap()
            .load(imgUrl)
            .placeholder(R.drawable.intersection)
            .error(R.drawable.intersection)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(iv_gallery)


    }
}