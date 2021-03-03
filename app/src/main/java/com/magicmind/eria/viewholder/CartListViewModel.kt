package com.magicmind.eria.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.magicmind.eria.R
import com.magicmind.eria.data.model.response.CartList
import com.magicmind.eria.data.model.response.MenuList

class CartListViewModel (itemView: View): RecyclerView.ViewHolder(itemView) {

    var iv_itemImage = itemView.findViewById(R.id.banner_image) as AppCompatImageView
    var tv_item_title = itemView.findViewById(R.id.tv_title) as AppCompatTextView
    var tvPrice = itemView.findViewById(R.id.tvPrice) as AppCompatTextView
    var ivRemoveItem = itemView.findViewById(R.id.ivminus) as AppCompatImageView
    var ivAddItem = itemView.findViewById(R.id.ivplus) as AppCompatImageView
    var tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity) as AppCompatTextView
    var ItemQuantityCount = 1


    fun bindItems(cartListDataItem: CartList) {
        var imgUrl = cartListDataItem.imagePath
        Glide.with(itemView.context)
            .asBitmap()
            .load(imgUrl)
            .placeholder(R.drawable.intersection)
            .error(R.drawable.intersection)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(iv_itemImage)


    }
}