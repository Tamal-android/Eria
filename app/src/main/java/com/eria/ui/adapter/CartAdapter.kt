package com.eria.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eria.R
import com.eria.data.model.response.CartList
import com.eria.ui.Interface.CartItemRemove
import com.eria.ui.base.HomeBaseActivity
import com.eria.ui.widget.SwipeControllerActions
import com.eria.viewholder.CartListViewModel

class CartAdapter(
    private val _Context: HomeBaseActivity,
    _cartListdata: ArrayList<CartList?>?,
    _cartItemRemoveListener: CartItemRemove

) :
    RecyclerView.Adapter<CartListViewModel>() {

    private var mContext: HomeBaseActivity = _Context
    private var _cartListdata: ArrayList<CartList>? = _cartListdata as ArrayList<CartList>?

    private var _cartItemRemoveListener: CartItemRemove? = _cartItemRemoveListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListViewModel {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.row_card, parent, false)
        return CartListViewModel(listItem)
    }

    override fun onBindViewHolder(holder: CartListViewModel, position: Int) {
//        holder.bindItems(_cartListdata!![position])

        holder.ivAddItem.setOnClickListener(View.OnClickListener {
            if (holder.ItemQuantityCount >= 1) {

                //   holder.btnAdd.visibility= View.GONE
                // holder.llMenuAdd.visibility= View.VISIBLE
                holder.ItemQuantityCount++
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
            } else if (holder.ItemQuantityCount < 1 || holder.ItemQuantityCount == 0) {
                // holder.btnAdd.visibility= View.VISIBLE
                // holder.llMenuAdd.visibility= View.GONE
                holder.ItemQuantityCount = 0
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
                _cartItemRemoveListener!!.onItemRemoved(position)
            }
        })
        holder.ivRemoveItem.setOnClickListener(View.OnClickListener {
            if (holder.ItemQuantityCount > 1) {

                //  holder.btnAdd.visibility= View.GONE
                // holder.llMenuAdd.visibility= View.VISIBLE
                --holder.ItemQuantityCount
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
            } else {
                //  holder.btnAdd.visibility= View.VISIBLE
                //  holder.llMenuAdd.visibility= View.GONE
                holder.ItemQuantityCount = 0
                holder.tvItemQuantity.text = holder.ItemQuantityCount.toString()
                _cartItemRemoveListener!!.onItemRemoved(position)
            }

        })

    }

    override fun getItemCount(): Int {
        return 10
    }


}