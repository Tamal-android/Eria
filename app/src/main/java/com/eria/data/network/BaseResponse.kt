package com.eria.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponse {
    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("message")
    @Expose
    var message: String? = null

    @JvmName("getStatus1")
    fun getStatus(): Int {
        return status
    }

    @JvmName("setStatus1")
    fun setStatus(status: Int) {
        this.status = status
    }

    @JvmName("getMessage1")
    fun getMessage(): String? {
        return message
    }

    @JvmName("setMessage1")
    fun setMessage(message: String?) {
        this.message = message
    }
}