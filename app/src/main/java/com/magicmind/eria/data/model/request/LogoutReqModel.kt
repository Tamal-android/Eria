package com.magicmind.eria.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LogoutReqModel (
    @SerializedName("device_token ")
    @Expose
    val device_token : String
)