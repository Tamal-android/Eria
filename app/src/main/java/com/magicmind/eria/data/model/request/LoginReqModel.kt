package com.magicmind.eria.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginReqModel (
    @SerializedName("country_prefix")
    @Expose
    var country_prefix :String,
    @SerializedName("mobile")
    @Expose
    var mobile : String,
    @SerializedName("role_id")
    @Expose
    var role_id  : Int
)