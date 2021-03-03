package com.magicmind.eria.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import com.magicmind.eria.R
import com.magicmind.eria.databinding.FragmentOrderDetailsBinding
import com.magicmind.eria.ui.base.BaseFragment
import com.magicmind.eria.ui.base.HomeBaseActivity
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.address_cart_view.view.*
import kotlinx.android.synthetic.main.address_cart_view.view.tvTag
import kotlinx.android.synthetic.main.order_item_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OrderDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderDetailsBinding
    private var baseActivity: HomeBaseActivity? = null

    companion object {
        fun newInstance(): OrderDetailsFragment {
            return OrderDetailsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = requireActivity() as HomeBaseActivity

    }

    override fun getFragmentActivityReference(activity: HomeBaseActivity) {
        this.baseActivity = activity
        baseActivity?.enableBackButton(true)
        baseActivity?.showBottomNavigationBar(false)
        baseActivity?.showHeader(true)
        baseActivity?.setToolbarTitle("Order Details")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseActivity?.enableBackButton(true)
        baseActivity?.showBottomNavigationBar(false)
        baseActivity?.showHeader(true)
        baseActivity?.setToolbarTitle("Order Details")

        addOrderLayout()


    }

    fun addOrderLayout(){
        for (i in 0..3) {
            val inflater =
                baseActivity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.order_item_layout, null)
            /*rowView.tag = i
            rowView.tvOrder_Id.text = ""
            rowView.tvOrder_price.text = ""
            rowView.tvOrder_date.text = ""
            rowView.tvOrder_items_count.text = ""
            rowView.tvDelevaryStatus.text = ""
            rowView.tv_Order_Repeat.text = ""*/
            rowView.llItem_Details.removeAllViews()
            for (j in 0..3){
                val inflater1 =
                    baseActivity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val sub_rowView: View = inflater1.inflate(R.layout.food_item_layout, null)
                rowView.llItem_Details.addView(sub_rowView)
            }
            rowView.btn_rate_order.setOnClickListener(View.OnClickListener {

            })
            binding.llOrderDetailsContainer.addView(rowView)
            //  val myView: View = layoutInflater.inflate(R.layout.address_cart_view, null)
        }
    }

    override fun onDestroyView() {
        baseActivity!!.setToolbarTitle("")
        baseActivity!!.showBottomNavigationBar(true)
        baseActivity?.showHeader(false)
        baseActivity?.setToolbarTitle("")
        baseActivity?.enableBackButton(false)
        super.onDestroyView()
    }


}