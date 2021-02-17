package com.eria.data.network

import com.eria.data.model.request.LoginReqModel
import com.eria.data.model.request.OTPReqModel
import com.eria.data.model.request.RegisterReqModel
import com.eria.data.model.response.LoginResponse
import com.eria.data.model.response.RegisterResponse
import com.eria.data.model.response.OTPVerifyResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiStores : ApiConfig{
    @POST(ApiConfig.API_LOGIN)
    fun callLoginApi(@Body loginReqModel: LoginReqModel?): Call<LoginResponse?>?

    @POST(ApiConfig.API_REGISTER)
    fun callRegisterApi(@Body registerReqModel: RegisterReqModel?): Call<RegisterResponse?>?

    @POST(ApiConfig.API_VERIFY_OTP)
    fun callOTPApi(@Body otpReqModel: OTPReqModel?): Call<OTPVerifyResponse?>?

    /*@POST(ApiConfig.API_FORGOT_PASSWORD)
    fun callForgotPasswordApi(@Body forgotPasswordReqModel: ForgotPasswordReqModel?): Call<ForgotPasswordResponse?>?

    @POST(ApiConfig.API_VERIFY_OTP)
    fun callVerifyOtpApi(@Body verifyOtpReqModel: VerifyOtpReqModel?): Call<VerifyOtpResponse?>?

    @POST(ApiConfig.API_RESET_PASSWORD)
    fun callResetPasswordApi(@Body resetPasswordReqModel: ResetPasswordReqModel?): Call<ChangeResetPasswordResponse?>?

    @GET(ApiConfig.API_LOCATION_LISTING)
    fun callLocationListApi(): Call<LocationListResponse?>?

    @POST(ApiConfig.API_FINDING_CAB)
    fun callFindingCabApi(@Body findingCabReqModel: FindingCabReqModel?): Call<FindingCabResponse?>?

    @POST(ApiConfig.API_BOOK_CAB)
    fun callBookCabApi(@Body bookCabReqModel: BookCabReqModel?): Call<BookCabResponse?>?

    @POST(ApiConfig.API_BOOK_CAB_SUCCESS)
    fun callBookCabSuccessApi(@Body bookCabSuccessReqModel: BookCabSuccessReqModel?): Call<BookCabSuccessResponse?>?

    @POST(ApiConfig.API_BOOK_CAB_FAILURE)
    fun callBookCabFailureApi(@Body bookCabFailureReqModel: BookCabSuccessReqModel?): Call<BookCabSuccessResponse?>?

    @POST(ApiConfig.API_DRIVER_START_RIDE)
    fun callstartRideApi(@Body startRiderequestModel: StartRiderequestModel?): Call<StartJourneyResponse?>?

    @POST(ApiConfig.API_DRIVER_END_RIDE)
    fun callendRideApi(@Body endRequestModel: EndRequestModel?): Call<EndRideResponse?>?

    @POST(ApiConfig.API_CANCEL_BOOKING)
    fun callCancelRideApi(@Body deleteRideReqModel: DeleteRideReqModel?): Call<DeleteRideResponse?>?

    @POST(ApiConfig.API_CHANGE_PASSWORD)
    fun callChangePasswordApi(@Body changePasswordReqModel: ChangePasswordReqModel?): Call<ChangeResetPasswordResponse?>?

    @GET(ApiConfig.API_GALLERY)
    fun callGalleryApi(): Call<GalleryResponse?>?

    @GET(ApiConfig.API_USER_PROFILE)
    fun callUserProfileApi(): Call<GetUserProfileResponse?>?

    @GET(ApiConfig.API_CONTACT_DETAILS)
    fun callcontactdetailsapi(): Call<ContactDetailsResponse?>?

    @POST(ApiConfig.API_RATING_REVIEW)
    fun callratingreviewapi(@Body reviewRequestModel: ReviewRequestModel?): Call<ReviewResponse?>?

    @GET(ApiConfig.API_RIDE_DETAILS)
    @Headers(["content-type: application/json"])
    fun calLRidedetailsApi(): Call<RideDetailsResponse?>?

    @POST(ApiConfig.API_DRIVER_RIDE_DETAILS)
    fun calLDriverRideDetailsApi(@Body driverRideRequestModel: DriverRideRequestModel?): Call<DriverRidesHistoryResponse?>?

    @Multipart
    @POST(ApiConfig.API_EDIT_PROFILE_INFORMATION)
    fun callUpdateProfileApi(
        @Part(ApiConfig.API_INPUT_NAME) name: RequestBody?,
        @Part(ApiConfig.API_INPUT_CONTACT_NO) contactNo: RequestBody?,
        @Part profileImage: MultipartBody.Part?
    ): Call<GetUserProfileResponse?>?

    @POST(ApiConfig.API_DRIVER_LOGIN)
    fun callDriverLoginApi(@Body loginDriverReqModel: LoginDriverReqModel?): Call<LoginDriverResponse?>?

    @POST(ApiConfig.API_INPUT_MAKE_TRANS)
    fun callTransapi(@Body makeTransactionModel: MakeTransactionModel?): Call<TransactionResponse?>?

    @POST(ApiConfig.API_UPDATED_VERSION)
    fun callupdatedversionapi(@Body updatedVersionModel: UpdatedVersionModel?): Call<UpdatedVersionResponse?>?

    @POST(ApiConfig.API_CURRENT_VERSION)
    fun callcurrentversionapi(@Body currentVersionModel: CurrentVersionModel?): Call<CurrentVersionResponse?>?*/
}