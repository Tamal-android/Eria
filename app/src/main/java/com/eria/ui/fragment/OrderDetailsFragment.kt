package com.eria.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.eria.R
import com.eria.databinding.FragmentOrderDetailsBinding
import com.eria.ui.base.BaseFragment
import com.eria.ui.base.HomeBaseActivity


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