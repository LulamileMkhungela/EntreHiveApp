package com.example.academy_intern.sampledesign.Services;

import com.example.academy_intern.sampledesign.Model.BaseResponse;
import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.Model.ItemProfile;
import com.example.academy_intern.sampledesign.Model.UserProfile;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {


//    @FormUrlEncoded
//    @POST("/user/save")
//    Call<UserProfile> register(@Field("identityNumber") String identityNumber,
//                                  @Field("name") String name,
//                                  @Field("username") String email,
//                                  @Field("password") String password);

    @FormUrlEncoded
    @POST("/userEvent/subscribeToEvent")
    Call<String> subscribeToEvent(@Field("userId") int userId, @Field("eventId") int eventId);

    @FormUrlEncoded
    @POST("/userEvent/cancelAttendance")
    Call<String> cancelAttendance(@Field("userId") int userId, @Field("eventId") int eventId);

    @FormUrlEncoded
    @POST("/event/acceptEvent")
    Call<EventProfile> acceptEvent(@Field("eventId") String eventId,
                           @Field("number_of_people") int number_of_people, @Field("attendance_points") int attendance_points);

    @FormUrlEncoded
    @POST("/user/registerUser")
    Call<UserProfile> register(@Field("identityNumber") String identityNumber,
                               @Field("name") String name,
                               @Field("username") String email,
                               @Field("password") String password,
                               @Field("deviceToken") String deviceToken);

    @GET("/event/getPendingEvents")
    Call<ArrayList<EventProfile>> getPendingEvents();

    @FormUrlEncoded
    @POST("/user/login")
    Call<UserProfile>login(@Field("username") String email,
                           @Field("password") String password,
                           @Field("deviceToken") String deviceToken);

    @FormUrlEncoded
    @POST("/event/save")
    Call<Integer>addEvent(@Field("eventTitle") String eventTitle,
                       @Field("eventDescription") String eventDescription,
                       @Field("eventLocation") String eventLocation,
                       @Field("userId") int id ,
                       @Field("eventDateTime") String eventDateTime);

    @GET("/event/getEventHistory")
    Call<ArrayList<EventProfile>> getEventHistory(@Query("userId") int userId);

    @GET("/event/getUpcomingEvents")
    Call<ArrayList<EventProfile>> getUpcomingEvents();

    @GET("/event/getPastEvents")
    Call<ArrayList<EventProfile>> getPastEvents();

    @GET("/user/getActiveUsers")
    Call<ArrayList<UserProfile>> getActiveUsers();

    @GET("/user/getInactiveUsers")
    Call<ArrayList<UserProfile>> getInactiveUsers();

    @GET("/downloadFile/{fileName:.+}")
    @Multipart
    Call<ResponseBody> getProfilePic(@Path("fileName") String fileName);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

    @GET("/user/getUserToken")
    Call<ResponseBody> getUserToken(@Query("userId") int userId);

    @FormUrlEncoded
    @POST("user/sendEmail")
    Call<UserProfile> sendEmailProcess(@Field("username") String username);

    @FormUrlEncoded
    @POST("user/sendResetCode")
    Call<UserProfile> sendResetCodeProcess(@Field("resetToken") String resetToken, @Field("resetCode") String resetCode);

    @FormUrlEncoded
    @POST("user/resetPassword")
    Call<UserProfile> resetPasswordProcess(@Field("resetToken") String resetToken, @Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("user/changePassword")
    Call<UserProfile> changePasswordProcess(@Field("userId") int userId, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("/user/checkBalance")
    Call<UserProfile> checkBalanceProcess(@Field("userId") int userId);

    @FormUrlEncoded
    @POST("/user/updateBalance")
    Call<UserProfile> updateBalanceProcess(@Field("userId") int userId, @Field("price") int itemPrice);

    @FormUrlEncoded
    @POST("/userEvent/attendingEvent")
    Call<EventProfile> allowUserAccess(@Field("userId") int userId, @Field("eventId") int eventId);

//    @Multipart
//    @POST("/uploadFile")
//    Call<ResponseBody> uploadFile(@Header("Authorization") String authorization,
//                                  @Part("token") RequestBody token,
//                                  @Part MultipartBody.Part file);

//    @Multipart
//    @POST("/uploadFile")
//    Call<ResponseBody> uploadFile(@Part("file") RequestBody file);

    @POST("/uploadFile")
    @Multipart
    Call<BaseResponse> uploadFile(@Part("userId") int userId, @Part MultipartBody.Part file);

    @POST("/uploadEventFile")
    @Multipart
    Call<String> uploadEventFile(@Part("userId") int userId, @Part MultipartBody.Part file);


    @POST("user/uploadProfilePic")
    @Multipart
    Call<BaseResponse> uploadProfilePic(@Part("userId") int userId, @Part MultipartBody.Part photo);

    @FormUrlEncoded
    @PUT("/user/updateUser")
    Call<UserProfile>updateUser(@Field("userId") int userId,
                                @Field("identityNumber") String identityNumber ,
                                @Field("name") String name,
                                @Field("username") String mail,
                                @Field("phoneNumber") String phoneNumber,
                                @Field("companyName") String companyName,
                                @Field("companyNumber") String companyNumber);

    @FormUrlEncoded
    @POST("/user/getUserDetails")
    Call<UserProfile>retrieveUserDetails(@Field("userId") int userId);

    @GET("/user/getAllUsers")
    Call<ArrayList<UserProfile>> fetchUsers();

    @FormUrlEncoded
    @POST("/user/getUserDetailsByEmail")
    Call<UserProfile>getUserDetailsByEmail(@Field("username") String username);

    @FormUrlEncoded
    @POST("/adminWallet/updateUserWallet")
    Call<String>updateWallet(@Field("points") int points);

    @GET("/adminWallet/getAdminBalance")
    Call<String> getBalance();

    @GET("/document/getDownloadLinks")
    Call<ArrayList<String>> getDownloadLinks(@Query("userId") int userId);

    @GET("/document/getDocumentNames")
    Call<ArrayList<String>> getDocumentNames(@Query("userId") int userId);

    @GET("/document/getEventDownloadLinks")
    Call<ArrayList<String>> getEventDownloadLinks(@Query("eventId") int eventId);

    @GET("/document/getEventDocumentNames")
    Call<ArrayList<String>> getEventDocumentNames(@Query("eventId") int eventId);

    @GET("/event/getMyEvents")
    Call<ArrayList<EventProfile>> getMyEvents(@Query("userId") int userId);

    @GET("/user/getTransactionHistory")
    Call<ArrayList<ItemProfile>> getTransactionHistory(@Query("userId") int userId);

    @GET("/user/getTopUsers")
    Call<ArrayList<UserProfile>> getTopUsers();

    @FormUrlEncoded
    @POST("/user/updateStatus")
    Call<UserProfile> acceptRegistration(@Field("userId") int userId, @Field("deviceToken") String deviceToken, @Field("message") String message);

    @FormUrlEncoded
    @POST("/user/sendToUser")
    Call<String> sentNotificationToUser(@Field("deviceToken") String deviceToken, @Field("message") String message);

    @FormUrlEncoded
    @GET("/event/getEventById")
    Call<EventProfile> getEventById(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("/user/makePurchase")
    Call<UserProfile> makePurchase(@Field("userId") int userId, @Field("itemName") String itemName, @Field("itemPrice") int itemPrice);

    @FormUrlEncoded
    @POST("/user/payUser")
    Call<String> allocatePoints(@Field("email") String email, @Field("points") int points);
}