package com.magicmind.eria.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.magicmind.eria.R
import com.magicmind.eria.databinding.FragmentViewPagerBinding
import com.magicmind.eria.ui.base.HomeBaseActivity
import com.magicmind.eria.utils.setImageFromRaw

class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding
    private var baseActivity: HomeBaseActivity? = null

    companion object {
        private const val ARG_POSITION = "ARG_POSITION"

        fun newInstance(position: Int) =
            ViewPagerFragment().apply {
                arguments = bundleOf(ARG_POSITION to position)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_POSITION)
        val onBoardingImages = getOnBoardAssetsLocation()
        with(binding) {
           // onBoardingImage.setImageFromRaw(onBoardingImages[position])
        }
    }


    private fun getOnBoardAssetsLocation(): List<Int> {
        val onBoardAssets: MutableList<Int> = ArrayList()
        onBoardAssets.add(R.raw.on_board_img1_land)
        onBoardAssets.add(R.raw.on_board_img2_land)
        onBoardAssets.add(R.raw.on_board_img3_land)
        return onBoardAssets
    }
}