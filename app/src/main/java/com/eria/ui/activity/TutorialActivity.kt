package com.eria.ui.activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.eria.R
import com.eria.ui.adapter.TutorialAdapter
import com.eria.ui.base.BaseActivity
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import kotlinx.android.synthetic.main.activity_tutorial.*
import java.util.*


class TutorialActivity : BaseActivity(), View.OnClickListener {
    private lateinit var view_pager_banner: ViewPager
    private lateinit var btn_order: Button
    private var currentPage = 0
    private var handler: Handler? = null
    private var update: Runnable? = null
    private var banner_images = intArrayOf(
        R.drawable.tutorial_first,
        R.drawable.tutorial_second,
        R.drawable.tutorial_third
    )
    private var banner_text = intArrayOf(
        R.string.tutorial_fragment_text,
        R.string.tutorial_fragment_text,
        R.string.tutorial_fragment_text
    )
    private var banner_title = intArrayOf(
        R.string.tutorial_fragment_title1,
        R.string.tutorial_fragment_title2,
        R.string.tutorial_fragment_title3
    )

    private lateinit var dots_indicator: DotsIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val edit: SharedPreferences.Editor = prefs.edit()
        edit.putBoolean(getString(R.string.pref_previously_started), java.lang.Boolean.TRUE)
        edit.commit()

        setViews()
        setvalues()
        slideBanners()
        initClickListener();

    }

    private fun initClickListener() {
        btn_order.setOnClickListener(this)
    }

    private fun setvalues() {
        view_pager!!.adapter = TutorialAdapter(this, banner_images,banner_title,banner_text)
        dots_indicator.setViewPager(view_pager)


    }

    private fun setViews() {
        view_pager_banner = findViewById<ViewPager>(R.id.view_pager)
        dots_indicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        btn_order = findViewById<Button>(R.id.btn_order)

    }


    private fun slideBanners() {
        currentPage = view_pager!!.currentItem
        handler = Handler()
        update = Runnable {
            if (currentPage == banner_images.size) {
                currentPage = 0
            }
            view_pager!!.setCurrentItem(currentPage++, true)
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler!!.post(update!!)
            }
        }, 1000, 3000)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_order -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                overridePendingTransition(R.anim.popup_in_anim, R.anim.popup_out_anim)
                startActivity(intent)
                finish()
            }


        }
    }

    override fun onResume() {
        super.onResume()

    }



}
