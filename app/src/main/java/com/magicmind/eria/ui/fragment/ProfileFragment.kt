package com.magicmind.eria.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.magicmind.eria.R
import com.magicmind.eria.databinding.FragmentProfileBinding
import com.magicmind.eria.ui.activity.AddressActivity
import com.magicmind.eria.ui.base.BaseFragment
import com.magicmind.eria.ui.base.HomeBaseActivity


class ProfileFragment : BaseFragment() {

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

    override fun getFragmentActivityReference(activity: HomeBaseActivity) {

       /* this.baseActivity = activity
        baseActivity?.showHeader(true)
        baseActivity?.enableBackButton(false)
        baseActivity?.setToolbarTitle("Profile")*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        /*baseActivity?.showHeader(true)
        baseActivity?.enableBackButton(false)
        baseActivity?.setToolbarTitle("Profile")*/
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity?.showHeader(true)
        baseActivity?.enableBackButton(false)
        baseActivity?.setToolbarTitle("Profile")
        binding.tvManageAddress.setOnClickListener(View.OnClickListener {
            movetoAddressBook()
        })

        binding.tvOrderDetails.setOnClickListener(View.OnClickListener {
            baseActivity?.changeFragment(OrderDetailsFragment.newInstance(),isAddToBackStack = true)
        })
    }



    fun movetoAddressBook() {
        val i = Intent(baseActivity, AddressActivity::class.java)
        baseActivity!!.overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
        startActivity(i)
    }
}