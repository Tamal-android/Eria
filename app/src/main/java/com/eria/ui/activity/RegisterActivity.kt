package com.eria.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.eria.R
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

                    moveToOtp()

                }
            }
        }
    }


    private fun moveToOtp() {
        var intent = Intent(this@RegisterActivity, OtpActivity::class.java)
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


}
