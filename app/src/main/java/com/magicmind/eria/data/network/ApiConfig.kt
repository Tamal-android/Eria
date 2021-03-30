package com.magicmind.eria.data.network

interface ApiConfig {
    companion object{
        //Timeout
        const val READ_TIMEOUT = 1200
        const val CONNECT_TIMEOUT = 1350
        const val WRITE_TIMEOUT = 1300

        //
        const val CALL_SUCCESS = 1
        const val CALL_FAILED_STATUS_0 = 0
        const val CALL_FAILED_STATUS_2 = 2
        const val CALL_FAILED_STATUS_3 = 3
        const val CALL_FAILED_STATUS_4 = 4
        const val CALL_FAILED_STATUS_5 = 5

        //Request Param Keys
        const val API_KEY = "apikey"
        const val ACCESS_TOKEN = "x-access-token"
        const val AUTHORIZATION = "Authorization"

        //Api
        const val API_LOGIN = "otp"
        const val API_REGISTER = "register"
        const val API_SLIDER = "sliders"
        const val API_RESTAURANTS = "restaurants"
        const val API_FORGOT_PASSWORD = "forgetpassword"
        const val API_VERIFY_OTP = "login"
        const val API_LOGOUT = "logout"
        const val API_RESET_PASSWORD = "resetpassword"
        const val API_LOCATION_LISTING = "locationlisting"
        const val API_CHANGE_PASSWORD = "locationlisting"
        const val API_USER_PROFILE = "getProfileInformation"
        const val API_EDIT_PROFILE_INFORMATION = "editProfileInformation"
        const val API_CURRENT_VERSION = "version/currentVersion"
        const val API_UPDATED_VERSION = "version/updateVersion"


        //Api Inputs
        const val API_INPUT_EMAIL = "email"
        const val API_INPUT_PASSWORD = "pwd"
        const val API_INPUT_NAME = "name"
        const val API_INPUT_PINCODE = "pincode"
        const val API_INPUT_CITY_NAME = "city_name"
        const val API_INPUT_CONTACT_NO = "contact_no"
        const val API_INPUT_IMAGE = "image_upload"
        const val API_INPUT_USER_ID = "user_id"
        const val API_INPUT_COVER_IMAGE = "cover_image"

    }



}