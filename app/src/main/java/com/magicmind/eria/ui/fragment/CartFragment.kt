package com.magicmind.eria.ui.fragment

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.magicmind.eria.R
import com.magicmind.eria.app.EriaApplication
import com.magicmind.eria.data.model.response.CartList
import com.magicmind.eria.data.model.response.MenuList
import com.magicmind.eria.databinding.FragmentCartBinding
import com.magicmind.eria.ui.Interface.CartItemRemove
import com.magicmind.eria.ui.activity.AddressActivity
import com.magicmind.eria.ui.adapter.CartAdapter
import com.magicmind.eria.ui.base.BaseFragment
import com.magicmind.eria.ui.base.HomeBaseActivity
import com.magicmind.eria.ui.widget.SwipeController
import com.magicmind.eria.ui.widget.SwipeControllerActions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CartFragment : BaseFragment() {

    private lateinit var binding: FragmentCartBinding
    private var baseActivity: HomeBaseActivity? = null
    var swipeController: SwipeController? = null

    companion object {
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = requireActivity() as HomeBaseActivity
    }

    override fun getFragmentActivityReference(activity: HomeBaseActivity) {


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        baseActivity?.showHeader(true)
        baseActivity?.enableBackButton(false)
        baseActivity?.showBottomNavigationBar(true)
        baseActivity?.changeHeaderColor(R.color.colorPrimary)
        baseActivity?.setToolbarTitle("Cart")
        baseActivity?.setToolbarTextColor(Color.WHITE)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var adapter: CartAdapter? = null

        val cartList = ArrayList<CartList?>()
        adapter = CartAdapter(baseActivity!!,cartList,object : CartItemRemove{
            override fun onItemRemoved(position: Int) {
                cartList.removeAt(position)
                adapter!!.notifyItemChanged(position)
                adapter!!.notifyItemRangeRemoved(position, 1)
                //adapter!!.notifyItemRemoved(position)
            }
        })
        binding.recyclerviewCard.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerviewCard.adapter = adapter

        for (i in 10 downTo 1) {
            var cart = CartList("", "abcd")
            cartList.add(cart)
        }
        adapter.notifyDataSetChanged()
        swipeController = SwipeController(requireContext(),object : SwipeControllerActions {
            override fun onRightClicked(position: Int) {
                Toast.makeText(requireContext(),cartList[position]!!.imageTitle,Toast.LENGTH_SHORT).show()
                cartList.removeAt(position)
                //adapter.notifyItemRemoved(position)
                adapter.notifyItemChanged(position)
                adapter.notifyItemRangeRemoved(position, 1)
               // adapter.notifyItemRangeChanged(position, adapter.itemCount)
                //adapter!!.notifyDataSetChanged()
                /*binding.recyclerviewCard.adapter.remove(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, adapter.getItemCount())*/

               // Toast.makeText(requireContext(),"SWIPED RIGHT",Toast.LENGTH_SHORT).show()
            }

            override fun onLeftClicked(position: Int) {
                super.onLeftClicked(position)
                //Toast.makeText(requireContext(),"SWIPED LEFT",Toast.LENGTH_SHORT).show()

            }
        })

        val itemTouchhelper = ItemTouchHelper(swipeController!!)
        itemTouchhelper.attachToRecyclerView(binding.recyclerviewCard)

        binding.recyclerviewCard.addItemDecoration(object : ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController!!.onDraw(c)
            }
        })
        GlobalScope.launch(Dispatchers.Main) {
            if (baseActivity?.db?.addressDao()!!.getAll().isNotEmpty() && EriaApplication.getPrefs().getAddressId(requireContext())>0) {
                binding.tvCurrentAddress.text = baseActivity?.db?.addressDao()?.loadAllByIds(
                    EriaApplication.getPrefs().getAddressId(
                        requireContext()
                    )
                )!!.Address
            }else{
                binding.tvCurrentAddress.text= "Add Address"
            }
        }

        binding.tvCurrentAddress.setOnClickListener {
            baseActivity?.movetoAddressBook()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        baseActivity?.showHeader(false)
    }

}