package com.eria.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eria.R
import com.eria.data.model.response.TopPicks
import com.eria.data.model.response.TopPicksResponse
import com.eria.databinding.FragmentDashboardBinding
import com.eria.ui.adapter.TopBrandsAdapter
import com.eria.ui.adapter.TopFoodsAdapter
import com.eria.ui.adapter.TopPicksAdapter
import com.eria.ui.base.HomeBaseActivity


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private var baseActivity: HomeBaseActivity? = null

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity?.showHeader(false)

        var adapter:TopBrandsAdapter ? = null

        adapter = TopBrandsAdapter(baseActivity!!)
        binding.recyclerviewBrand.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,
                false)
        binding.recyclerviewBrand.adapter = adapter

        var adapter1:TopFoodsAdapter ? = null

        adapter1 = TopFoodsAdapter(baseActivity!!)
        binding.recyclerviewFood.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,
                false)
        binding.recyclerviewFood.adapter = adapter1

        var adapter2:TopPicksAdapter ? = null

        val toppicksList = ArrayList<TopPicks>()
        adapter2 = TopPicksAdapter(baseActivity!!,toppicksList)
        binding.recyclerviewTop.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,
                false)
        binding.recyclerviewTop.adapter = adapter2
        for (i in 10 downTo 1) {
            var movie = TopPicks("", "abcd")
            toppicksList.add(movie)
        }
        adapter2.notifyDataSetChanged()
    }

}