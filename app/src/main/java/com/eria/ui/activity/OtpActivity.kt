package com.eria.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import com.eria.R
import com.eria.databinding.ActivityOtpBinding
import com.eria.ui.base.BaseActivity
import com.eria.ui.base.HomeBaseActivity


class OtpActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOtpBinding
    private lateinit var otpEditTexts: Array<AppCompatEditText>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)

        initOtpEditTexts()
       initClickListener()


    }

    private fun initClickListener() {
        binding.btnComplete.setOnClickListener(this)

    }

    private fun initOtpEditTexts() {
        otpEditTexts =
            arrayOf(binding.etOtpNo1, binding.etOtpNo2, binding.etOtpNo3, binding.etOtpNo4)

        binding.etOtpNo1.addTextChangedListener(OtpTextWatcher(0))
        binding.etOtpNo2.addTextChangedListener(OtpTextWatcher(1))
        binding.etOtpNo3.addTextChangedListener(OtpTextWatcher(2))
        binding.etOtpNo4.addTextChangedListener(OtpTextWatcher(3))

        binding.etOtpNo1.setOnKeyListener(OtpOnKeyListener(0))
        binding.etOtpNo2.setOnKeyListener(OtpOnKeyListener(1))
        binding.etOtpNo3.setOnKeyListener(OtpOnKeyListener(2))
        binding.etOtpNo4.setOnKeyListener(OtpOnKeyListener(3))
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

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_complete -> {
                var intent = Intent(this, HomeBaseActivity::class.java)
                overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                startActivity(intent)
                finish()
            }
        }
    }



}