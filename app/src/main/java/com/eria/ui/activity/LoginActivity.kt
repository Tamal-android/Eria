package com.eria.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.eria.R
import com.eria.app.AppData
import com.eria.app.EriaApplication
import com.eria.data.model.request.LoginReqModel
import com.eria.data.model.response.LoginRegisterResponse
import com.eria.data.network.ApiCallback
import com.eria.data.network.ApiConfig
import com.eria.databinding.ActivityLoginBinding
import com.eria.ui.base.BaseActivity


class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val signup = SpannableString("Create an account Sign Up")

        signup.setSpan(TextAppearanceSpan(this, R.font.roboto_regular), 0, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        signup.setSpan(TextAppearanceSpan(this, R.font.roboto_bold), 19, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvSignUp.setText(signup, TextView.BufferType.SPANNABLE)

        binding.ccp.setOnCountryChangeListener { selectedCountry ->
            showToast(selectedCountry.phoneCode)

        }

        initClickListener()

    }




     private fun initClickListener() {
         binding.btnSendOtp.setOnClickListener(this)
         binding.tvSignUp.setOnClickListener(this)

     }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_send_otp ->
                validateInput()

            R.id.tv_sign_up ->
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

                    moveToOtp()

                }
            }
        }
    }

    private fun callLoginApi(loginReqModel: LoginReqModel) {
        showProgress(getString(R.string.txt_progress_loading))
        val loginApiCall = getWebService().callLoginApi(loginReqModel)
        loginApiCall!!.enqueue(object : ApiCallback<LoginRegisterResponse>() {
            override fun onFinish() {
                hideProgress()
            }
            override fun onSuccess(loginRegisterResponse: LoginRegisterResponse?) {
                when (loginRegisterResponse?.status) {
//                    ApiConfig.CALL_SUCCESS -> {
//                        showToast(loginRegisterResponse.message!!)
//                      //  saveUserDataInPref(loginRegisterResponse)
//                        // moveToDashboard()
//                    }
//                    ApiConfig.CALL_FAILED_STATUS_0 -> showToast(loginRegisterResponse.message!!)
//                    ApiConfig.CALL_FAILED_STATUS_2 -> showToast(loginRegisterResponse.message!!)
//                    ApiConfig.CALL_FAILED_STATUS_3 -> showToast(loginRegisterResponse.message!!)
//                    ApiConfig.CALL_FAILED_STATUS_4 -> showToast(loginRegisterResponse.message!!)
//                    ApiConfig.CALL_FAILED_STATUS_5 -> showToast(loginRegisterResponse.message!!)
                    else -> showToast(getString(R.string.error_something_went_wrong))
                }
            }

            override fun onFailure(code: Int, msg: String?) {
                showToast(msg!!)
            }

            override fun onThrowable(t: Throwable?) {
                showToast(getString(R.string.error_parse))
            }

        })
    }

    private fun moveToOtp() {
        var intent = Intent(this@LoginActivity, OtpActivity::class.java)
        overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
        startActivity(intent)
    }

    /*private fun saveUserDataInPref(loginRegisterResponse: LoginRegisterResponse) {

        EriaApplication.getPrefs().setUserId(this@LoginActivity,
            loginRegisterResponse.loginData?.customerDetails?.id
        )

        EriaApplication.getPrefs().setFcmToken(
            this@LoginActivity,
            loginRegisterResponse.loginData?.customerDetails?.accessToken
        )

        EriaApplication.getPrefs().setUserName(
            this@LoginActivity,
            loginRegisterResponse.loginData?.customerDetails?.name
        )

        EriaApplication.getPrefs().setSignInMobile(
            this@LoginActivity,
            loginRegisterResponse.loginData?.customerDetails?.contactNo
        )
        EriaApplication.getPrefs().setUserEmail(
            this@LoginActivity,
            loginRegisterResponse.loginData?.customerDetails?.email
        )
        AppData.ACCESS_TOKEN =
            loginRegisterResponse.loginData?.customerDetails?.accessToken.toString()
    }*/

  /*  private fun moveToForgotPassword() {
        var intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
        startActivity(intent)
    }


*/
  private fun moveToCreateAccount() {
      var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
      overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
      startActivity(intent)
  }

}
