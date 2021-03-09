package com.magicmind.eria.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.magicmind.eria.R
import com.magicmind.eria.databinding.FragmentSearchBinding
import com.magicmind.eria.ui.adapter.SearchAdapter
import com.magicmind.eria.ui.base.BaseFragment
import com.magicmind.eria.ui.base.HomeBaseActivity


class SearchFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchBinding
    private var baseActivity: HomeBaseActivity? = null

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity?.showHeader(true)
        baseActivity?.changeHeaderColor(R.color.white)
        baseActivity?.setToolbarTitle("Search")
        baseActivity?.setToolbarTextColor(Color.BLACK)
        var adapter: SearchAdapter? = null

        binding.svSearch.requestFocus();
        binding.svSearch.setOnClickListener(View.OnClickListener {
            binding.svSearch.isIconified = false
        })
        adapter = SearchAdapter(baseActivity!!)
        binding.recyclerviewSearch.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,
            false)
        binding.recyclerviewSearch.adapter = adapter


    }
    override fun onDestroyView() {
        super.onDestroyView()
        baseActivity?.showHeader(false)
    }

}