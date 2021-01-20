package com.eria.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.eria.R
import com.eria.databinding.FragmentProfileBinding
import com.eria.ui.base.HomeBaseActivity


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var baseActivity: HomeBaseActivity? = null

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity?.showHeader(true)
        baseActivity?.setToolbarTitle("Profile")

    }
    override fun onDestroyView() {
        super.onDestroyView()
        baseActivity?.showHeader(false)
    }
}