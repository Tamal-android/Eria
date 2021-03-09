package com.magicmind.eria.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.magicmind.eria.data.network.BaseResponse

class LogoutResponse : BaseResponse<LogoutDataModel>()
data class LogoutDataModel(
    @SerializedName("body")
    @Expose
    val body: String? = null
)
