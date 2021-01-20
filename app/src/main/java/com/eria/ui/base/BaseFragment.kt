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
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment

abstract class BaseFragment<T : ViewDataBinding> : DaggerFragment() {

    private lateinit var mViewDataBinding: T
    private var rootView: View? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initView(view: View)

    abstract fun initClickListener()

    abstract fun getDataFromArguments()

    protected abstract fun initViewModel()

    abstract fun observeViewModel()

    open fun getBinding(): T? {
        return mViewDataBinding
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
        initViewModel()
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        rootView = mViewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initClickListener()
    }


    open fun toggleProgressLoading(isShow: Boolean) {
    }
}