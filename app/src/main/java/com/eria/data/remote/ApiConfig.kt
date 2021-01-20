package com.yo.rides.data.remote

class ApiConfig {

    companion object {
        const val API_BASE_URL = "http://www.theyorides.in/api/Staging/"

        const val CONNECT_TIME_OUT: Long = 40
        const val READ_TIME_OUT: Long = 40
        const val WRITE_TIME_OUT: Long = 40

        //Mime Type
        const val MIME_TYPE_TEXT_PLAIN: String = "text/plain"
        const val MIME_TYPE_MULTIPART_FORM_DATA: String = "multipart/form-data"
    }
}