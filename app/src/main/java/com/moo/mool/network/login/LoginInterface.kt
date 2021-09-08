package com.moo.mool.network.login

import com.moo.mool.model.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface LoginInterface {
    @POST("/api/v1/login")
    suspend fun requestLogin(@Body body : RequestLogin) : ResponseLogin

    @PUT("/api/v1/member/token")
    suspend fun requestTokenRefresh(@Body body : RequestTokenRefresh) : ResponseTokenRefresh

    @PUT("/api/v1/member/reset")
    suspend fun requestResetPassword(@Body body : RequestResetPassword) : String
}