package com.eria.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.eria.R
import com.eria.data.model.response.MenuList
import com.eria.ui.Interface.onMenuItemAdd
import com.eria.ui.base.HomeBaseActivity
import com.eria.ui.fragment.MenuListFragment
import com.eria.viewholder.MenuListViewHolder

class MenuListAdapter(
    private val _context: HomeBaseActivity,
    _menuListdata: List<MenuList?>?,
    _onItemAddedListener: onMenuItemAdd
) :
    RecyclerView.Adapter<MenuListViewHolder>() {

    private var mContext: HomeBaseActivity = _context
    private var _menuListdata: List<MenuList>? = _menuListdata as List<MenuList>?

    private var _onItemAddedListener: onMenuItemAdd? = _onItemAddedListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.menu_list_item, parent, false)
        return MenuListViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        holder.bindItems(_menuListdata!![position])
        holder.btnAdd.setOnClickListener(View.OnClickListener {
            Toast.makeText(mContext!!, "Selected " + _menuListdata!![position], Toast.LENGTH_SHORT)
                .show()
            if (holder.llMenuAdd.visibility==View.GONE){
                holder.llMenuAdd.visibility=View.VISIBLE
                holder.btnAdd.visibility=View.GONE
                holder.ItemQuantityCount=1
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
                _onItemAddedListener?.onItemAdded(true)
            }else{
                holder.btnAdd.visibility=View.VISIBLE
                holder.llMenuAdd.visibility=View.GONE
                holder.ItemQuantityCount=0
                holder.tvItemQuantity.text =  holder.ItemQuantityCount.toString()
                _onItemAddedListener?.onItemAdded(false)
            }
        })
        holder.ivAddItem.setOnClickListener(View.OnClickListener {
            if (holder.ItemQuantityCount>=1){

                holder.btnAdd.visibility=View.GONE
                holder.llMenuAdd.visibility=View.VISIBLE
                holder.ItemQuantityCount++
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
                _onItemAddedListener?.onItemAdded(true)
            }else if (holder.ItemQuantityCount<1 || holder.ItemQuantityCount==0){
                holder.btnAdd.visibility=View.VISIBLE
                holder.llMenuAdd.visibility=View.GONE
                holder.ItemQuantityCount=0
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
                _onItemAddedListener?.onItemAdded(false)
            }
        })
        holder.ivRemoveItem.setOnClickListener(View.OnClickListener {
            if (holder.ItemQuantityCount>1){

                holder.btnAdd.visibility=View.GONE
                holder.llMenuAdd.visibility=View.VISIBLE
                --holder.ItemQuantityCount
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
                _onItemAddedListener?.onItemAdded(true)
            }else{
                holder.btnAdd.visibility=View.VISIBLE
                holder.llMenuAdd.visibility=View.GONE
                holder.ItemQuantityCount=0
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
                _onItemAddedListener?.onItemAdded(false)
            }

        })
        holder.itemView.setOnClickListener(View.OnClickListener {
          /*  Toast.makeText(mContext!!, "Selected " + _menuListdata!![position], Toast.LENGTH_SHORT)
                .show()*/

        })
    }

    override fun getItemCount(): Int {
        return 10
    }


}