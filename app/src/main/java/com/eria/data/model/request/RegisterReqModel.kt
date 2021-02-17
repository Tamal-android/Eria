package com.eria.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterReqModel (
    @SerializedName("name")
    @Expose
    val name :String,
    @SerializedName("email")
    @Expose
    var email :String,
    @SerializedName("mobile_no")
    @Expose
    var mobile_no : String,
    @SerializedName("country_code")
    @Expose
    var country_code :String
)
