package com.eria.data.network;

/**
 * <p>
 * Configuration class to store API tags & configuration
 */

public interface ApiConfig {

    //Timeout
    long READ_TIMEOUT = 1200;
    long CONNECT_TIMEOUT = 1350;
    long WRITE_TIMEOUT = 1300;

    //
    int CALL_SUCCESS = 1;
    int CALL_FAILED_STATUS_0 = 0;
    int CALL_FAILED_STATUS_2 = 2;
    int CALL_FAILED_STATUS_3 = 3;
    int CALL_FAILED_STATUS_4 = 4;
    int CALL_FAILED_STATUS_5 = 5;


    //Request Param Keys
    String API_KEY = "apikey";
    String ACCESS_TOKEN = "x-access-token";
    String AUTHORIZATION = "Authorization";


    //Api
    String API_LOGIN = "login";
    String API_REGISTER = "register";
    String API_FORGOT_PASSWORD = "forgetpassword";
    String API_VERIFY_OTP = "verifyotp";
    String API_RESET_PASSWORD = "resetpassword";
    String API_LOCATION_LISTING = "locationlisting";
    String API_FINDING_CAB = "findingcab";
    String API_CHANGE_PASSWORD = "changepassword";
    String API_GALLERY = "gallery";
    String API_USER_PROFILE = "getProfileInformation";
    String API_CONTACT_DETAILS = "contact";
    String API_EDIT_PROFILE_INFORMATION = "editProfileInformation";
    String API_BOOK_CAB = "bookcab";
    String API_BOOK_CAB_SUCCESS = "bookcab/success";
    String API_BOOK_CAB_FAILURE = "bookcab/failure";
    String API_RIDE_DETAILS = "bookinghistorylist";
    String API_DRIVER_RIDE_DETAILS = "driver_ride/ridelst";
    String API_CANCEL_BOOKING = "cancelbooking";
    String API_RATING_REVIEW = "reviewrating";
    String API_DRIVER_LOGIN = "driverlogin";
    String API_DRIVER_START_RIDE = "driver_ride/startride";
    String API_DRIVER_END_RIDE = "driver_ride/endride";
    String API_CURRENT_VERSION = "version/currentVersion";
    String API_UPDATED_VERSION = "version/updateVersion";



    //Api Inputs
    String API_INPUT_EMAIL = "email";
    String API_INPUT_PASSWORD = "pwd";
    String API_INPUT_NAME = "name";
    String API_INPUT_PINCODE = "pincode";
    String API_INPUT_CITY_NAME = "city_name";
    String API_INPUT_CONTACT_NO = "contact_no";
    String API_INPUT_IMAGE = "image_upload";
    String API_INPUT_USER_ID = "user_id";
    String API_INPUT_COVER_IMAGE = "cover_image";
    String API_INPUT_DRIVER_ID = "driver_id";
    String API_INPUT_MAKE_TRANS = "bookcab/makeTransaction";



}
