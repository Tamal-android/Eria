package com.magicmind.eria.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.magicmind.eria.R
import com.magicmind.eria.app.EriaApplication
import com.magicmind.eria.data.model.request.LoginReqModel
import com.magicmind.eria.data.model.request.LogoutReqModel
import com.magicmind.eria.data.model.response.LoginResponse
import com.magicmind.eria.data.model.response.LogoutResponse
import com.magicmind.eria.data.network.ApiCallback
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
        baseActivity?.showBottomNavigationBar(true)
        baseActivity?.changeHeaderColor(R.color.colorPrimary)
        baseActivity?.setToolbarTitle("Profile")
        baseActivity?.setToolbarTextColor(Color.WHITE)



        binding.tvManageAddress.setOnClickListener(View.OnClickListener {

            baseActivity?.movetoAddressBook()
        })

        binding.tvOrderDetails.setOnClickListener(View.OnClickListener {
            baseActivity?.changeFragment(
                OrderDetailsFragment.newInstance(),
                isAddToBackStack = true
            )
        })
        binding.tvLogout.setOnClickListener(View.OnClickListener {
            /*callLogoutApi(
                LogoutReqModel(
                    EriaApplication.getPrefs().getFcmToken(
                        requireContext()
                    )!!
                )
            )*/
        })
    }

    private fun callLogoutApi(logoutReqModel: LogoutReqModel) {
        baseActivity?.showProgress(getString(R.string.txt_progress_loading))
        val loginApiCall = baseActivity?.getWebService()!!.callLogoutApi(logoutReqModel)
        loginApiCall!!.enqueue(object : ApiCallback<LogoutResponse>() {
            override fun onFinish() {
                baseActivity?.hideProgress()
            }

            override fun onSuccess(logoutResponse: LogoutResponse?) {
                if (logoutResponse?.status == false) {
                    /*    Log.e("ggg", loginResponse.data?.body.toString())
                        EriaApplication.appPrefs.setMobile_no(
                            requireContext(),
                            loginResponse.data?.mobile_no
                        )
                        Log.e("LOGOUT",  EriaApplication.appPrefs.getMobile_no(requireContext())!!)
                        Toast.makeText(requireContext(),  EriaApplication.getPrefs().getMobile_no(requireContext()), Toast.LENGTH_SHORT).show()*/
                } else {
                    Toast.makeText(requireContext(), logoutResponse?.message, Toast.LENGTH_LONG)
                        .show()
                }

            }

            override fun onFailure(code: Int, msg: String?) {
                Log.e("Logout", "$msg :$code")
                if (code == 403) {
                    baseActivity?.showToast("User not registered")
                } else
                    baseActivity?.showToast(msg!!)
            }

            override fun onThrowable(t: Throwable?) {
                baseActivity?.showToast(getString(R.string.error_parse))
            }

        })
    }


}