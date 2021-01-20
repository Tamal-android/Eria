package com.eria.data.model.response

import com.eria.data.network.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRegisterResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    var loginData: LoginRegisterData? = null

    @JvmName("setLoginData1")
    fun setLoginData(loginData: LoginRegisterData?) {
        this.loginData = loginData
    }

    @JvmName("getLoginData1")
    fun getLoginData(): LoginRegisterData? {
        return loginData
    }
}