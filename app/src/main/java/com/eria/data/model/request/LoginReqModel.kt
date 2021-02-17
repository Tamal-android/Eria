package com.eria.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginReqModel (
    @SerializedName("mobile_no")
    @Expose
    var mobile_no : String,
    @SerializedName("country_code")
    @Expose
    var country_code :String
)