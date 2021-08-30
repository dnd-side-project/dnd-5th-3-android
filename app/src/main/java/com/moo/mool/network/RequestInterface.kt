package com.moo.mool.network

import com.moo.mool.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {
    @POST("/api/v1/login")
    fun requestLogin(@Body body : RequestLogin) : Call<ResponseLogin>

    @POST("/api/v1/member")
    fun requestMember(@Body body : RequestMember) : Call<ResponseMember>

    @PUT("/api/v1/member")
    fun requestProfileUpdate(@Header("Authorization") accessToken: String, @Body body : RequestProfileUpdate) : Call<ResponseMember>

    @HTTP(method = "DELETE", path = "/api/v1/member", hasBody = true)
    fun requestDeleteAccount(@Header("Authorization") accessToken: String, @Body body : RequestDeleteAccount) : Call<ResponseMember>

    @POST("/api/v1/member/check/password")
    fun requestCheckOldPassword(@Header("Authorization") accessToken: String, @Body body : RequestOldPasswordCheck) : Call<String>

    @PUT("/api/v1/member/token")
    fun requestTokenRefresh(@Body body : RequestTokenRefresh) : Call<ResponseTokenRefresh>

    @Multipart
    @POST("/api/v1/posts")
    fun requestUploadPost(@Part("title") title: String, @Part("content") content: String, @Part file: MultipartBody.Part?) : Call<ResponseUploadPost>

    @GET("/api/v1/member/exists/{email}/email")
    fun requestDuplicateCheckEmail(@Path("email") email : String) : Call<String>

    @GET("/api/v1/member/exists/{name}/name")
    fun requestDuplicateCheckNickname(@Path("name") name : String) : Call<String>

    @GET("/api/v1/notice")
    fun requestNoticeList() : Call<ResponseNotice>

    @GET("/api/v1/notice/{noticeId}")
    fun requestNoticePost(@Path("noticeId") noticeId: String) : Call<Notice>
}