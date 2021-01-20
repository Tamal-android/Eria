package com.eria.data.model.request

class LoginReqModel {
    private var phoneNo = ""
    private var fcm_id = ""
    private var device_type = ""
    private var device_token = ""

    fun LoginReqModel(phoneNo: String, fcm_id: String, device_type: String, device_token: String) {
        this.phoneNo = phoneNo
        this.fcm_id = fcm_id
        this.device_type = device_type
        this.device_token = device_token
    }

    fun getFcm_id(): String? {
        return fcm_id
    }

    fun setFcm_id(fcm_id: String) {
        this.fcm_id = fcm_id
    }

    fun getDevice_type(): String? {
        return device_type
    }

    fun setDevice_type(device_type: String) {
        this.device_type = device_type
    }

    fun getDevice_token(): String? {
        return device_token
    }

    fun setDevice_token(device_token: String) {
        this.device_token = device_token
    }


    fun LoginReqModel() {}


    fun getPhoneNo(): String? {
        return phoneNo
    }

    fun setPhoneNo(phoneNo: String) {
        this.phoneNo = phoneNo
    }
}