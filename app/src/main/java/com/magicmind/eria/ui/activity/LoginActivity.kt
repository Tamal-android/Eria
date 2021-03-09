package com.magicmind.eria.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.magicmind.eria.R
import com.magicmind.eria.app.EriaApplication
import com.magicmind.eria.data.model.request.LoginReqModel
import com.magicmind.eria.data.model.response.LoginResponse
import com.magicmind.eria.data.network.ApiCallback
import com.magicmind.eria.databinding.ActivityLoginBinding
import com.magicmind.eria.ui.base.BaseActivity


class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val SpanString = SpannableString(
            "By Registering you agree to our Terms of Use and Privacy Policy"
        )
        val signup = SpannableString("Create an account Sign Up")

        signup.setSpan(
            TextAppearanceSpan(this, R.font.roboto_regular),
            0,
            18,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        signup.setSpan(
            TextAppearanceSpan(this, R.font.roboto_bold),
            19,
            25,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val teremsAndCondition: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
               /* val mIntent: Intent = Intent(this, CommonWebView::class.java)
                mIntent.putExtra("isTermsAndCondition", true)
                startActivity(mIntent)*/

                Toast.makeText(this@LoginActivity, "Terms", Toast.LENGTH_SHORT).show()
            }
        }

        // Character starting from 32 - 45 is Terms and condition.
        // Character starting from 49 - 63 is privacy policy.


        // Character starting from 32 - 45 is Terms and condition.
        // Character starting from 49 - 63 is privacy policy.
        val privacy: ClickableSpan = object : ClickableSpan() {

            override fun onClick(widget: View) {
                Toast.makeText(this@LoginActivity, "Policies", Toast.LENGTH_SHORT).show()
                /* val mIntent: Intent = Intent(this, CommonWebView::class.java)
               mIntent.putExtra("isPrivacyPolicy", true)
               startActivity(mIntent)*/
            }
        }

        SpanString.setSpan(teremsAndCondition, 32, 45, 0)
        SpanString.setSpan(privacy, 49, 63, 0)
        SpanString.setSpan(ForegroundColorSpan(Color.WHITE), 32, 45, 0)
        SpanString.setSpan(ForegroundColorSpan(Color.WHITE), 49, 63, 0)
        SpanString.setSpan(UnderlineSpan(), 32, 45, 0)
        SpanString.setSpan(UnderlineSpan(), 49, 63, 0)
        SpanString.setSpan(StyleSpan(Typeface.BOLD), 32, 45, 0)
        SpanString.setSpan(StyleSpan(Typeface.BOLD), 49, 63, 0)

        binding.tvTermsPolicy.movementMethod = LinkMovementMethod.getInstance()
        binding.tvTermsPolicy.setText(SpanString, TextView.BufferType.SPANNABLE)
        binding.tvTermsPolicy.isSelected = true

        binding.tvSignUp.setText(signup, TextView.BufferType.SPANNABLE)

        binding.ccp.setOnCountryChangeListener { selectedCountry ->
            //showToast(selectedCountry.phoneCode)

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
                    //moveToOtp()
                    if (isConnected(this@LoginActivity)) {
                        callLoginApi(
                            LoginReqModel(
                                binding.ccp.selectedCountryCodeWithPlus,
                                binding.etPhoneNumber.text.toString(),
                                2
                            )
                        )
                    }else
                        showError(getString(R.string.no_internet))
                }
            }
        }
    }

    private fun callLoginApi(loginReqModel: LoginReqModel) {
        showProgress(getString(R.string.txt_progress_loading))
        val loginApiCall = getWebService().callLoginApi(loginReqModel)
        loginApiCall!!.enqueue(object : ApiCallback<LoginResponse>() {
            override fun onFinish() {
                hideProgress()
            }

            override fun onSuccess(loginResponse: LoginResponse?) {
                if (loginResponse?.status == false) {
                    Log.e("ggg", loginResponse.data?.body.toString())
                    EriaApplication.appPrefs.setMobile_no(
                        this@LoginActivity,
                        loginResponse.data?.mobile_no
                    )
                    Log.e("ggg123",  EriaApplication.appPrefs.getMobile_no(this@LoginActivity)!!)
                    Toast.makeText(this@LoginActivity,  EriaApplication.getPrefs().getMobile_no(this@LoginActivity), Toast.LENGTH_SHORT).show()
                    moveToOtp()
                }else{
                    Toast.makeText(this@LoginActivity, loginResponse?.message, Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(code: Int, msg: String?) {
                Log.e(this@LoginActivity.javaClass.name, "$msg :$code")
                if (code == 403) {
                    showToast("User not registered")
                } else
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
