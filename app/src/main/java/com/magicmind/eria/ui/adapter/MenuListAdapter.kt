package com.magicmind.eria.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.magicmind.eria.R
import com.magicmind.eria.data.model.response.MenuList
import com.magicmind.eria.databinding.FragmentMenuListBinding
import com.magicmind.eria.ui.Interface.onMenuItemAdd
import com.magicmind.eria.ui.base.HomeBaseActivity
import com.magicmind.eria.ui.fragment.MenuListFragment
import com.magicmind.eria.viewholder.MenuListViewHolder

class MenuListAdapter(
    private val _context: HomeBaseActivity,
    var binding1: FragmentMenuListBinding,
    _menuListdata: List<MenuList?>?
) :
    RecyclerView.Adapter<MenuListViewHolder>() {

    private var mContext: HomeBaseActivity = _context
    var binding: FragmentMenuListBinding= binding1
    private var _menuListdata: List<MenuList>? = _menuListdata as List<MenuList>?

  //  private var _onItemAddedListener: onMenuItemAdd? = _onItemAddedListener
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
            binding.viewModel!!.count.value= binding.viewModel!!.count.value?.plus(1)
            binding.viewModel!!.menuItem.value=_menuListdata!![position]
            if (holder.llMenuAdd.visibility==View.GONE){
                holder.llMenuAdd.visibility=View.VISIBLE
                holder.btnAdd.visibility=View.GONE
                holder.ItemQuantityCount=1
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
               // _onItemAddedListener?.onItemAdded(true,binding.viewModel!!.count.value)
            }else{
                holder.btnAdd.visibility=View.VISIBLE
                holder.llMenuAdd.visibility=View.GONE
                holder.ItemQuantityCount=0
                holder.tvItemQuantity.text =  holder.ItemQuantityCount.toString()
                //_onItemAddedListener?.onItemAdded(false,binding.viewModel!!.count.value)
            }
        })
        holder.ivAddItem.setOnClickListener(View.OnClickListener {
            binding.viewModel!!.count.value= binding.viewModel!!.count.value?.plus(1)
            binding.viewModel!!.menuItem.value= _menuListdata!![position]
            if (holder.ItemQuantityCount>=1){

                holder.btnAdd.visibility=View.GONE
                holder.llMenuAdd.visibility=View.VISIBLE
                holder.ItemQuantityCount++
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
               // _onItemAddedListener?.onItemAdded(true,binding.viewModel!!.count.value)
            }else if (holder.ItemQuantityCount<1 || holder.ItemQuantityCount==0){
                holder.btnAdd.visibility=View.VISIBLE
                holder.llMenuAdd.visibility=View.GONE
                holder.ItemQuantityCount=0
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
               // _onItemAddedListener?.onItemAdded(false,binding.viewModel!!.count.value)
            }
        })
        holder.ivRemoveItem.setOnClickListener(View.OnClickListener {
            binding.viewModel!!.count.value= binding.viewModel!!.count.value?.minus(1)
            if (holder.ItemQuantityCount>1){

                holder.btnAdd.visibility=View.GONE
                holder.llMenuAdd.visibility=View.VISIBLE
                --holder.ItemQuantityCount
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
               // _onItemAddedListener?.onItemAdded(true,binding.viewModel!!.count.value)
            }else{
                holder.btnAdd.visibility=View.VISIBLE
                holder.llMenuAdd.visibility=View.GONE
                holder.ItemQuantityCount=0
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
               // _onItemAddedListener?.onItemAdded(false,binding.viewModel!!.count.value)
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