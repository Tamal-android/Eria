package com.magicmind.eria.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magicmind.eria.R
import kotlinx.android.synthetic.main.menu_addon_bottom_sheet_layout.*

class BottomSheetFragment : BottomSheetDialogFragment() {
    private var fragmentView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.menu_addon_bottom_sheet_layout, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        tvItemName.setOnClickListener {
            Toast.makeText(context, "Item Name", Toast.LENGTH_SHORT).show()
        }
        tvQuantity.setOnClickListener {
            Toast.makeText(context, "Quantity", Toast.LENGTH_SHORT).show()
        }
        tvOptionName.setOnClickListener {
            Toast.makeText(context, "Option Name", Toast.LENGTH_SHORT).show()
        }
        llAdonItemContainer.setOnClickListener {
            Toast.makeText(context, "Option Name", Toast.LENGTH_SHORT).show()
        }

    }
}