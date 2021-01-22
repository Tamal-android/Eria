package com.eria.data.network

import com.eria.data.model.response.LoginRegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiCallback<M> : Callback<LoginRegisterResponse?> {
    abstract fun onSuccess(model: LoginRegisterResponse?)

    abstract fun onFailure(code: Int, msg: String?)

    abstract fun onThrowable(t: Throwable?)

    abstract fun onFinish()

    override fun onResponse(
        call: Call<LoginRegisterResponse?>,
        response: Response<LoginRegisterResponse?>
    ) {
        if (response.isSuccessful) {
            onSuccess(response.body())
        } else {
            onFailure(response.code(), response.errorBody().toString())
        }
        onFinish()
    }

    override fun onFailure(call: Call<LoginRegisterResponse?>, t: Throwable) {
        onThrowable(t)
        onFinish()
    }
}