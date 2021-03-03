package com.magicmind.eria.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.magicmind.eria.R
import com.magicmind.eria.databinding.ActivitySplashBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.magicmind.eria.app.EriaApplication

class SplashActivity: AppCompatActivity() {

    private val SPLASH_TIMEOUT: Long = 2500
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        //binding.ivLogo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move))
        getFCMToken()
        displaySplashScreen()
    }


    fun displaySplashScreen() {

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
            val edit: SharedPreferences.Editor = prefs.edit()
            val previouslyStarted: Boolean = prefs.getBoolean(getString(R.string.pref_previously_started), false)
            if (!previouslyStarted)
            {
                val i = Intent(this@SplashActivity, TutorialActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                startActivity(i)
                finish()
            }
            else
            {
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                startActivity(i)
                finish()
            }

        }, SPLASH_TIMEOUT)
    }

    fun getFCMToken():String{
        var token=""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
            EriaApplication.getPrefs().setFcmToken(
                this,
                token
            )
            // Log and toast
            //val msg = getString(R.string.msg_token_fmt, token)
            Log.e("TAG", token)
           // Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
        return token
    }


}