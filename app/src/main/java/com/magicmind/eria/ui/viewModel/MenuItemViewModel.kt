package com.magicmind.eria.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magicmind.eria.data.model.response.MenuList

class MenuItemViewModel : ViewModel() {

    val count: MutableLiveData<Int> = MutableLiveData(0)
    val individualItemcount: MutableLiveData<Int> = MutableLiveData(0)
    val menuItem: MutableLiveData<MenuList> = MutableLiveData()
}