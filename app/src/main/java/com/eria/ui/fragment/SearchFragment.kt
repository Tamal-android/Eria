package com.eria.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eria.R
import com.eria.databinding.FragmentSearchBinding
import com.eria.ui.adapter.SearchAdapter
import com.eria.ui.base.BaseFragment
import com.eria.ui.base.HomeBaseActivity


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

        baseActivity?.showHeader(false)
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
        baseActivity?.showHeader(false)
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