package com.moo.mool.network.login

import com.moo.mool.model.*
import javax.inject.Inject

class LoginServiceImpl @Inject constructor(
    private val loginInterface: LoginInterface
) : LoginService {
    override suspend fun requestLogin(body: RequestLogin): ResponseLogin =
        loginInterface.requestLogin(body)

    override suspend fun requestTokenRefresh(body: RequestTokenRefresh): ResponseTokenRefresh =
        loginInterface.requestTokenRefresh(body)

    override suspend fun requestResetPassword(body: RequestResetPassword): String =
        loginInterface.requestResetPassword(body)
}