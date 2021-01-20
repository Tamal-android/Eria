package com.eria.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.eria.R
import com.eria.databinding.ActivitySplashBinding

class SplashActivity: AppCompatActivity() {

    private val SPLASH_TIMEOUT: Long = 3000
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        //binding.ivLogo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move))
        displaySplashScreen()
    }


    fun displaySplashScreen() {

        val handler = Handler()
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


}