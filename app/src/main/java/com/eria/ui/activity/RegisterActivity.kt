package com.eria.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.eria.R
import com.eria.app.AppData
import com.eria.app.EriaApplication
import com.eria.data.model.request.LoginReqModel
import com.eria.data.model.request.RegisterReqModel
import com.eria.data.model.response.LoginRegisterResponse
import com.eria.data.network.ApiCallback
import com.eria.data.network.ApiConfig
import com.eria.databinding.ActivityRegisterBinding
import com.eria.ui.base.BaseActivity
import java.security.AccessController.getContext


class RegisterActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        val signup = SpannableString("You Have an account Sign In")

        signup.setSpan(
            TextAppearanceSpan(this, R.font.roboto_regular),
            0,
            20,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        signup.setSpan(
            TextAppearanceSpan(this, R.font.roboto_bold),
            21,
            25,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvSign.setText(signup, TextView.BufferType.SPANNABLE)
        binding.ccp.setOnCountryChangeListener { selectedCountry ->
            showToast(selectedCountry.phoneCode)

        }

        initClickListener()
    }


    private fun initClickListener() {
        binding.btnSendOtp.setOnClickListener(this)
        binding.tvSign.setOnClickListener(this)


    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_send_otp ->
                validateInput()
            R.id.tv_sign ->
                moveToCreateAccount()
        }
    }

    private fun validateInput() {
        with(binding) {

            when {
                TextUtils.isEmpty(binding.etPhoneNumber.text.toString()) -> {
                    binding.etPhoneNumber.error = getString(R.string.error_phone)
                    binding.etPhoneNumber.isFocusable = true
                    binding.etPhoneNumber.isFocusableInTouchMode = true
                    binding.etPhoneNumber.requestFocus()
                    return
                }


                else -> {

                    //moveToOtp()
                    binding.ccp.defaultCountryCodeWithPlus

                    callRegisterApi(
                        RegisterReqModel(
                            binding.etFullName.text.toString(),
                            binding.etEmail.text.toString(),
                            binding.etPhoneNumber.text.toString(),
                            binding.ccp.defaultCountryCodeWithPlus
                        )
                    )
                }
            }
        }
    }

    private fun callRegisterApi(registerReqModel: RegisterReqModel) {
        showProgress(getString(R.string.txt_progress_loading))
        val loginApiCall = getWebService().callRegisterApi(registerReqModel)
        loginApiCall!!.enqueue(object : ApiCallback<LoginRegisterResponse>() {
            override fun onFinish() {
                hideProgress()
            }

            override fun onSuccess(loginRegisterResponse: LoginRegisterResponse?) {

                //when (loginRegisterResponse?.status) {
                loginRegisterResponse?.data?.let { Log.e("ggg", it.toString()) }
                saveUserDataInPref(loginRegisterResponse)
                showToast(loginRegisterResponse?.message!!)
                moveToOtp(loginRegisterResponse?.data?.otp)

                /* ApiConfig.CALL_SUCCESS -> {
                            showToast(loginRegisterResponse.message!!)
                           // saveUserDataInPref(loginRegisterResponse)
                            //moveToOtp()
                        }
                        ApiConfig.CALL_FAILED_STATUS_0 -> showToast(loginRegisterResponse.message!!)
                        ApiConfig.CALL_FAILED_STATUS_2 -> showToast(loginRegisterResponse.message!!)
                        ApiConfig.CALL_FAILED_STATUS_3 -> showToast(loginRegisterResponse.message!!)
                        ApiConfig.CALL_FAILED_STATUS_4 -> showToast(loginRegisterResponse.message!!)
                        ApiConfig.CALL_FAILED_STATUS_5 -> showToast(loginRegisterResponse.message!!)*/
                // else -> showToast(getString(R.string.error_something_went_wrong))
                // }
            }

            override fun onFailure(code: Int, msg: String?) {
                showToast(msg!!)
            }

            override fun onThrowable(t: Throwable?) {
                showToast(getString(R.string.error_parse))
            }

        })
    }

    private fun saveUserDataInPref(loginRegisterResponse: LoginRegisterResponse?) {

        Log.e(this.javaClass.name,loginRegisterResponse?.data?.user_id!!)
        EriaApplication.getPrefs().setUserId(
            this@RegisterActivity,
            Integer.parseInt(loginRegisterResponse?.data?.user_id)
        )

        EriaApplication.getPrefs().setOTP(
            this@RegisterActivity,
            loginRegisterResponse?.data?.otp
        )
        EriaApplication.getPrefs().setMobile_no(
            this@RegisterActivity,
            loginRegisterResponse?.data?.mobeli_no
        )
        EriaApplication.getPrefs().setUserEmail(
            this@RegisterActivity,
            loginRegisterResponse?.data?.email
        )

    }

    private fun moveToOtp(otp: String?) {
        var intent = Intent(this@RegisterActivity, OtpActivity::class.java).putExtra("otp",otp).putExtra("userId",EriaApplication.appPrefs.getUserId(this))
        overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
        startActivity(intent)
    }


    /*  private fun moveToForgotPassword() {
          var intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
          overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
          startActivity(intent)
      }

      private fun moveToCreateAccount() {
          var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
          overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
          startActivity(intent)
      }
  */


    private fun moveToCreateAccount() {
        var intent = Intent(this, LoginActivity::class.java)
        overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
