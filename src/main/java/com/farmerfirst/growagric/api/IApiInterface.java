package com.farmerfirst.growagric.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface IApiInterface {

    String PING_URL = "www.freknur.com";

    String BASE_URL = "https://ac8f-35-203-89-227.ngrok-free.app/";

    String TERMS_URL = "https://growagric.com/terms-and-conditions.html";

    String REGISTER_API = "api/v1/users/register";
    @Headers("Content-Type: application/json")
    @POST(REGISTER_API)
    Call<ResponseBody> register(@Body String registerApiPayload);

    String LOGIN_API = "api/v1/users/login";
    @Headers("Content-Type: application/json")
    @POST(LOGIN_API)
    Call<ResponseBody> login(@Body String loginApiPayload);

    String ADD_FARM_API = "api/v1/users/addFarm";
    @Headers("Content-Type: application/json")
    @POST(ADD_FARM_API)
    Call<ResponseBody> addFarm(@Body String addFarmApiPayload);

    String ADD_FINANCE_API = "api/v1/users/addFinance";
    @Headers("Content-Type: application/json")
    @POST(ADD_FINANCE_API)
    Call<ResponseBody> addFinance(@Body String addFinanceApiPayload);

    String VERIFY_PHONE_API = "api/v1/users/verifyPhoneNumber";
    @Headers("Content-Type: application/json")
    @POST(VERIFY_PHONE_API)
    Call<ResponseBody> verifyPhone(@Body String verifyPhoneApiPayload);

    String CHANGE_PASSWORD_API = "api/v1/users/changePassword";
    @Headers("Content-Type: application/json")
    @POST(CHANGE_PASSWORD_API)
    Call<ResponseBody> changePassword(@Body String changePasswordApiPayload);

    String ACCOUNT_VERIFIED_API = "api/v1/users/accountVerified/{phoneNumber}";
    @GET(ACCOUNT_VERIFIED_API)
    Call<ResponseBody> getAccountVerificationState(@Path("phoneNumber") String phoneNumber);

    String PROFILE_API = "api/v1/users/profile/{phoneNumber}";
    @GET(PROFILE_API)
    Call<ResponseBody> profile(@Path("phoneNumber") String phoneNumber);

    String PROFILE_STATUS_API = "api/v1/users/profileStatus/{phoneNumber}";
    @GET(PROFILE_STATUS_API)
    Call<ResponseBody> getProfileStatus(@Path("phoneNumber") String phoneNumber);

    String MODIFY_USER_API = "api/v1/users/modify/{uuid}";
    @Headers("Content-Type: application/json")
    @PUT(MODIFY_USER_API)
    Call<ResponseBody> modifyUser(@Path("uuid") String uuid,@Body String modifyUserPayload);

    String FCM_USER_API = "api/v1/users/fcm/{uuid}";
    @Headers("Content-Type: application/json")
    @PUT(FCM_USER_API)
    Call<ResponseBody> setFcm(@Path("uuid") String uuid,@Body String fcmPayload);

    String RESEND_OTP_API ="api/v1/users/otp/{phoneNumber}";
    @GET(RESEND_OTP_API)
    Call<ResponseBody> sendOTP(@Path("phoneNumber") String phoneNumber);

    String HEAR_ABOUT_US_API = "api/v1/users/hearAboutUs";
    @Headers("Content-Type: application/json")
    @POST(HEAR_ABOUT_US_API)
    Call<ResponseBody> addHearAboutUsSource(@Body String addHearAboutBoutUsApiPayload);

    String FIELD_AGENT_FARMER_LIST_API ="api/v1/users/fieldAgentUserList";
    @GET(FIELD_AGENT_FARMER_LIST_API)
    Call<ResponseBody> getFarmerList(@Query("page") int page,@Query("size") int size);

    String FIELD_AGENT_FARM_LIST_API ="api/v1/users/fieldAgentFarmList";
    @GET(FIELD_AGENT_FARM_LIST_API)
    Call<ResponseBody> getFarmList(@Query("page") int page,@Query("size") int size);

    String SEND_CHAT_MESSAGE_API = "api/v1/users/chatMessage";
    @Headers("Content-Type: application/json")
    @POST(SEND_CHAT_MESSAGE_API)
    Call<ResponseBody> sendMessage(@Body String sendMessageApiPayload);

    String LEARNING_MODULE_API = "api/v1/users/learnModule/{phoneNumber}";
    @GET(LEARNING_MODULE_API)
    Call<ResponseBody> getModuleList(@Path("phoneNumber") String phoneNumber);

    String LEARNING_COURSE_API = "api/v1/users/learnCourse/{phoneNumber}";
    @GET(LEARNING_COURSE_API)
    Call<ResponseBody> getCourseList(@Path("phoneNumber") String phoneNumber);

    String BOOK_KEEPING_API = "api/v1/users/addFarmRecord";
    @Headers("Content-Type: application/json")
    @POST(BOOK_KEEPING_API)
    Call<ResponseBody> addFarmRecord(@Body String addFarmRecordApiPayload);

    String INVITE_API = "api/v1/users/addInvite";
    @Headers("Content-Type: application/json")
    @POST(INVITE_API)
    Call<ResponseBody> addInvite(@Body String addFarmRecordApiPayload);

    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String url);

    String UPLOAD_IMAGE_API = "api/v1/users/uploadResource";
    @Multipart
    @POST(UPLOAD_IMAGE_API)
    Call<ResponseBody> uploadImageFile(@Part MultipartBody.Part image,
                                       @Part("user_uuid") RequestBody user_uuid,
                                       @Part("name") RequestBody name,
                                       @Part("description") RequestBody desciption,
                                       @Part("action") RequestBody action);

}