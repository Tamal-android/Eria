package com.eria.data.network;



import com.eria.data.model.request.LoginReqModel;
import com.eria.data.model.request.RegisterReqModel;
import com.eria.data.model.response.LoginRegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * <p>
 * ApiStores will be used for store API/Webservice retrofit methods to connect with backend web.
 */

public interface ApiStores {

    @POST(ApiConfig.API_LOGIN)
    Call<LoginRegisterResponse> callLoginApi(@Body LoginReqModel loginReqModel);

    @POST(ApiConfig.API_REGISTER)
    Call<LoginRegisterResponse> callRegisterApi(@Body RegisterReqModel registerReqModel);

    /*@POST(ApiConfig.API_FORGOT_PASSWORD)
    Call<ForgotPasswordResponse> callForgotPasswordApi(@Body ForgotPasswordReqModel forgotPasswordReqModel);

    @POST(ApiConfig.API_VERIFY_OTP)
    Call<VerifyOtpResponse> callVerifyOtpApi(@Body VerifyOtpReqModel verifyOtpReqModel);

    @POST(ApiConfig.API_RESET_PASSWORD)
    Call<ChangeResetPasswordResponse> callResetPasswordApi(@Body ResetPasswordReqModel resetPasswordReqModel);

    @GET(ApiConfig.API_LOCATION_LISTING)
    Call<LocationListResponse> callLocationListApi();

    @POST(ApiConfig.API_FINDING_CAB)
    Call<FindingCabResponse> callFindingCabApi(@Body FindingCabReqModel findingCabReqModel);

    @POST(ApiConfig.API_BOOK_CAB)
    Call<BookCabResponse> callBookCabApi(@Body BookCabReqModel bookCabReqModel);

    @POST(ApiConfig.API_BOOK_CAB_SUCCESS)
    Call<BookCabSuccessResponse> callBookCabSuccessApi(@Body BookCabSuccessReqModel bookCabSuccessReqModel);

    @POST(ApiConfig.API_BOOK_CAB_FAILURE)
    Call<BookCabSuccessResponse> callBookCabFailureApi(@Body BookCabSuccessReqModel bookCabFailureReqModel);

    @POST(ApiConfig.API_DRIVER_START_RIDE)
    Call<StartJourneyResponse> callstartRideApi(@Body StartRiderequestModel startRiderequestModel);

    @POST(ApiConfig.API_DRIVER_END_RIDE)
    Call<EndRideResponse> callendRideApi(@Body EndRequestModel endRequestModel);

    @POST(ApiConfig.API_CANCEL_BOOKING)
    Call<DeleteRideResponse> callCancelRideApi(@Body DeleteRideReqModel deleteRideReqModel);

    @POST(ApiConfig.API_CHANGE_PASSWORD)
    Call<ChangeResetPasswordResponse> callChangePasswordApi(@Body ChangePasswordReqModel changePasswordReqModel);

    @GET(ApiConfig.API_GALLERY)
    Call<GalleryResponse> callGalleryApi();

    @GET(ApiConfig.API_USER_PROFILE)
    Call<GetUserProfileResponse> callUserProfileApi();

    @GET(ApiConfig.API_CONTACT_DETAILS)
    Call<ContactDetailsResponse> callcontactdetailsapi();

    @POST(ApiConfig.API_RATING_REVIEW)
    Call<ReviewResponse> callratingreviewapi(@Body ReviewRequestModel reviewRequestModel);

    @GET(ApiConfig.API_RIDE_DETAILS)
    @Headers({"content-type: application/json"})
    Call<RideDetailsResponse> calLRidedetailsApi();

    @POST(ApiConfig.API_DRIVER_RIDE_DETAILS)
    Call<DriverRidesHistoryResponse> calLDriverRideDetailsApi(@Body DriverRideRequestModel driverRideRequestModel);

    @Multipart
    @POST(ApiConfig.API_EDIT_PROFILE_INFORMATION)
    Call<GetUserProfileResponse> callUpdateProfileApi(@Part(ApiConfig.API_INPUT_NAME) RequestBody name,
                                                      @Part(ApiConfig.API_INPUT_CONTACT_NO) RequestBody contactNo,
                                                      @Part MultipartBody.Part profileImage);

    @POST(ApiConfig.API_DRIVER_LOGIN)
    Call<LoginDriverResponse> callDriverLoginApi(@Body LoginDriverReqModel loginDriverReqModel);

    @POST(ApiConfig.API_INPUT_MAKE_TRANS)
    Call<TransactionResponse> callTransapi(@Body MakeTransactionModel makeTransactionModel);

    @POST(ApiConfig.API_UPDATED_VERSION)
    Call<UpdatedVersionResponse> callupdatedversionapi(@Body UpdatedVersionModel updatedVersionModel);

    @POST(ApiConfig.API_CURRENT_VERSION)
    Call<CurrentVersionResponse> callcurrentversionapi(@Body CurrentVersionModel currentVersionModel);*/
}
