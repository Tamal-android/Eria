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
   /* fun RegisterReqModel(
        name: String,
        email: String,
        mobile_no: String,
        country_code: String
    ) {
        this.name = name
        this.email = email
        this.country_code = country_code
        this.mobile_no = mobile_no
    }



    fun RegisterReqModel() {}

    fun get
    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

}*/
