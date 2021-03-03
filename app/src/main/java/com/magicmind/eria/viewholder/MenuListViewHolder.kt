package com.magicmind.eria.viewholder

import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.magicmind.eria.R
import com.magicmind.eria.data.model.response.MenuList

class MenuListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var iv_itemImage = itemView.findViewById(R.id.banner_image) as AppCompatImageView
        var tv_item_title = itemView.findViewById(R.id.tv_item_title) as AppCompatTextView
        var tv_item_desc = itemView.findViewById(R.id.tv_item_desc) as AppCompatTextView
        var btnAdd = itemView.findViewById(R.id.btnAdd) as AppCompatButton
        var llMenuAdd = itemView.findViewById(R.id.llMenuAdd) as LinearLayoutCompat
        var ivRemoveItem = itemView.findViewById(R.id.ivRemoveItem) as AppCompatImageView
        var ivAddItem = itemView.findViewById(R.id.ivAddItem) as AppCompatImageView
        var tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity) as AppCompatTextView
        var ItemQuantityCount = 0


        fun bindItems(menuListDataItem: MenuList) {
            var imgUrl = menuListDataItem.imagePath
            Glide.with(itemView.context)
                .asBitmap()
                .load(imgUrl)
                .placeholder(R.drawable.intersection)
                .error(R.drawable.intersection)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(iv_itemImage)


        }
}