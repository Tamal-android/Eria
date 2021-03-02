package com.eria.ui.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import java.util.regex.Pattern

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity != null && activity is HomeBaseActivity) {
            val baseActivity = activity as HomeBaseActivity?
            getFragmentActivityReference(baseActivity!!)
        }
    }

    protected abstract fun getFragmentActivityReference(activity: HomeBaseActivity)

}