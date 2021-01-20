package com.eria.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eria.R
import com.eria.databinding.FragmentCartBinding
import com.eria.databinding.FragmentProfileBinding
import com.eria.ui.adapter.CardAdapter
import com.eria.ui.adapter.TopBrandsAdapter
import com.eria.ui.base.HomeBaseActivity


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private var baseActivity: HomeBaseActivity? = null

    companion object {
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = requireActivity() as HomeBaseActivity
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity?.showHeader(true)
        baseActivity?.setToolbarTitle("Cart")
        var adapter: CardAdapter? = null

        adapter = CardAdapter(baseActivity!!)
        binding.recyclerviewCard.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,
            false)
        binding.recyclerviewCard.adapter = adapter


    }
    override fun onDestroyView() {
        super.onDestroyView()
        baseActivity?.showHeader(false)
    }

}