package com.moo.mool.network.login

import com.moo.mool.model.*

interface LoginService {
    suspend fun requestLogin(body : RequestLogin) :ResponseLogin
    suspend fun requestTokenRefresh(body : RequestTokenRefresh) : ResponseTokenRefresh
    suspend fun requestResetPassword(body : RequestResetPassword) : String
}