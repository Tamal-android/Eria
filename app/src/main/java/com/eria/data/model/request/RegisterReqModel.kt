package com.eria.data.model.request

class RegisterReqModel {
    private var name = ""
    private var email = ""
    private var contact_no = ""
    private var fcm_id = ""
    private var device_type = ""
    private var device_token = ""

    fun RegisterReqModel(
        name: String,
        email: String,
        contact_no: String,
        fcm_id: String,
        device_type: String,
        device_token: String
    ) {
        this.name = name
        this.email = email
        this.contact_no = contact_no
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


    fun RegisterReqModel() {}

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


    fun getContact_no(): String? {
        return contact_no
    }

    fun setContact_no(contact_no: String) {
        this.contact_no = contact_no
    }
}