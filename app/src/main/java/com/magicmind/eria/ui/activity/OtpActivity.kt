package com.magicmind.eria.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import com.magicmind.eria.R
import com.magicmind.eria.app.AppData
import com.magicmind.eria.app.EriaApplication
import com.magicmind.eria.data.model.request.OTPReqModel
import com.magicmind.eria.data.model.response.OTPVerifyResponse
import com.magicmind.eria.data.network.ApiCallback
import com.magicmind.eria.databinding.ActivityOtpBinding
import com.magicmind.eria.ui.base.BaseActivity
import java.util.concurrent.TimeUnit


class OtpActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOtpBinding
    private lateinit var otpEditTexts: Array<AppCompatEditText>
    var otp: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)
        if (intent.getStringExtra("otp").toString() != null)
            otp = intent.getStringExtra("otp").toString() + "  " + intent.getIntExtra("userId", 0)
                .toString()
        Log.e(this.javaClass.name, otp!!)
        initOtpEditTexts()
        initClickListener()
        setOtpTimer()


    }
    private val FORMAT = "%02d:%02d"

    var seconds = 0
    var minutes:Int = 0

    private fun setOtpTimer() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResendOtp.isEnabled=false
                binding.tvResendOtpTimer.text = String.format(
                    FORMAT,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                );
            }

            override fun onFinish() {
                binding.tvResendOtp.isEnabled=true
                binding.tvResendOtpTimer.text = "now!"
            }
        }.start()
    }

    private fun initClickListener() {
        binding.btnComplete.setOnClickListener(this)

    }

    private fun initOtpEditTexts() {
        otpEditTexts =
            arrayOf(
                binding.etOtpNo1,
                binding.etOtpNo2,
                binding.etOtpNo3,
                binding.etOtpNo4
            )

        binding.etOtpNo1.addTextChangedListener(OtpTextWatcher(0))
        binding.etOtpNo2.addTextChangedListener(OtpTextWatcher(1))
        binding.etOtpNo3.addTextChangedListener(OtpTextWatcher(2))
        binding.etOtpNo4.addTextChangedListener(OtpTextWatcher(3))
       // binding.etOtpNo5.addTextChangedListener(OtpTextWatcher(4))

        binding.etOtpNo1.setOnKeyListener(OtpOnKeyListener(0))
        binding.etOtpNo2.setOnKeyListener(OtpOnKeyListener(1))
        binding.etOtpNo3.setOnKeyListener(OtpOnKeyListener(2))
        binding.etOtpNo4.setOnKeyListener(OtpOnKeyListener(3))
       // binding.etOtpNo5.setOnKeyListener(OtpOnKeyListener(4))
    }


    inner class OtpTextWatcher(private val currentIndex: Int = 0) : TextWatcher {
        private var isFirst = false
        private var isLast: Boolean = false
        private var newTypedString = ""

        init {
            if (currentIndex == 0)
                isFirst = true
            else if (currentIndex == otpEditTexts.size - 1)
                this.isLast = true
        }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            newTypedString = s.subSequence(start, start + count).toString().trim { it <= ' ' }
        }

        override fun afterTextChanged(s: Editable?) {
            var text = newTypedString

            /* Detect paste event and set first char */
            if (text.length > 1)
                text = text[0].toString() // TODO: We can fill out other EditTexts
            otpEditTexts[currentIndex].removeTextChangedListener(this)
            otpEditTexts[currentIndex].setText(text)
            otpEditTexts[currentIndex].setSelection(text.length)
            otpEditTexts[currentIndex].addTextChangedListener(this)
            if (text.length == 1)
                moveToNext()
            else if (text.isEmpty())
                moveToPrevious()
        }

        private fun moveToNext() {
            if (!isLast)
                otpEditTexts[currentIndex + 1].requestFocus()

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                otpEditTexts[currentIndex].clearFocus()
                hideKeyboard()
            }
        }

        private fun moveToPrevious() {
            if (!isFirst)
                otpEditTexts[currentIndex - 1].requestFocus()
        }

        private fun isAllEditTextsFilled(): Boolean {
            for (editText in otpEditTexts) {
                if (editText.text.toString().trim { it <= ' ' }.isEmpty())
                    return false
            }
            return true
        }

        private fun hideKeyboard() {
            if (currentFocus != null) {
                val inputMethodManager: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
        }
    }


    inner class OtpOnKeyListener internal constructor(private val currentIndex: Int) :
        View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action === KeyEvent.ACTION_DOWN) {
                if (otpEditTexts.get(currentIndex).text.toString()
                        .isEmpty() && currentIndex != 0
                ) otpEditTexts.get(currentIndex - 1).requestFocus()
            }
            return false
        }

    }

    fun getDeviceId(context: Context): String? {
        val deviceId: String = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return deviceId
    }
    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }


    private fun capitalize(s: String?): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_complete -> {
                Log.e(this.javaClass.name,  EriaApplication.getPrefs().getMobile_no(this@OtpActivity)!!)

                if (isConnected(this)) {
                    EriaApplication.getPrefs().getMobile_no(this@OtpActivity)?.let {
                        callOTPApi(
                            OTPReqModel(
                                it,
                                otpEditTexts[0].text.toString() + otpEditTexts[1].text.toString() + otpEditTexts[2].text.toString() + otpEditTexts[3].text.toString(),
                                getDeviceId(this)!!,
                                getDeviceName()!!,
                                "Android",
                                EriaApplication.appPrefs.getFcmToken(this)!!
                            )
                        )
                    }

                }else{
                    showError(getString(R.string.no_internet))
                }
                //moveToLocation()
            }
        }
    }

    private fun callOTPApi(otpReqModel: OTPReqModel) {
        showProgress(getString(R.string.txt_progress_loading))
        val otpApiCall = getWebService().callOTPApi(otpReqModel)
        otpApiCall!!.enqueue(object : ApiCallback<OTPVerifyResponse>() {
            override fun onFinish() {
                hideProgress()
            }

            override fun onSuccess(model: OTPVerifyResponse?) {
                Toast.makeText(this@OtpActivity,model?.message,Toast.LENGTH_LONG).show()
                if (model?.status == false){
                    saveUserDataInPref(model)
                    moveToLocation()

                }else{
                    EriaApplication.getPrefs().setUserLoggedIn(
                        this@OtpActivity,
                        false
                    )
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

    private fun saveUserDataInPref(otpVerifyResponse: OTPVerifyResponse) {

            EriaApplication.getPrefs().setMobile_no(
                this@OtpActivity,
                otpVerifyResponse.data?.user!!.mobile
            )

            EriaApplication.getPrefs().setBEARER_TOKEN(
            this@OtpActivity,
                otpVerifyResponse.data?.token
        )

        EriaApplication.getPrefs().setUserId(
            this@OtpActivity,
            otpVerifyResponse.data?.user?.userId
        )

        EriaApplication.getPrefs().setSTATUS(
            this@OtpActivity,
            otpVerifyResponse.data?.user?.status
        )
        EriaApplication.getPrefs().setUserLoggedIn(
            this@OtpActivity,
            true
        )

        AppData.ACCESS_TOKEN =
            otpVerifyResponse?.data?.token!!
    }
    private fun moveToLocation() {
        var intent = Intent(this@OtpActivity, LocationActivity::class.java)
        overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}