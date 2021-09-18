package com.moo.mool.repository

import android.content.Context
import com.moo.mool.database.SharedManager
import com.moo.mool.database.User
import com.moo.mool.model.*
import com.moo.mool.network.login.LoginServiceImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LoginRepository @Inject constructor(
   private val loginServiceImpl: LoginServiceImpl,
   @ApplicationContext val context: Context
) {
    private val sharedManager : SharedManager by lazy { SharedManager(context) }

    suspend fun autoLogin(): ResponseTokenRefresh {
        sharedManager.setSkipOnBoardingFragment() // 자동 로그인시 온보딩페이지 SKIP할수 있도록 설정
        // refreshToken 정보가 있는 경우
        val currentUser = sharedManager.getCurrentUser()
        val response = loginServiceImpl.requestTokenRefresh(
            RequestTokenRefresh(
                currentUser.email.toString(),
                currentUser.refreshToken.toString(),
            ),
        )
        sharedManager.updateAccessToken(response.accessToken)
        return response
    }

    suspend fun loginUser(body: RequestLogin): ResponseLogin {
        val response : ResponseLogin = loginServiceImpl.requestLogin(RequestLogin(
            body.email,
            body.password
        ))

        // 로그인 성공 후 토큰 저장
        val currentUser = User().apply {
            accessToken = response!!.accessToken
            refreshToken = response!!.refreshToken
            nickname = response!!.name
            email = response!!.email

        }
        sharedManager.saveCurrentUser(currentUser) // SharedPreference에 저장

        return response
    }

    suspend fun resetPassword(email: String) =
        loginServiceImpl.requestResetPassword(RequestResetPassword(email))

    suspend fun setGoogleLoginResponse(response: ResponseLogin) {
        val currentUser = User().apply {
            accessToken = response.accessToken
            refreshToken = response.refreshToken
            nickname = response.name
            email = response.email
        }
        sharedManager.saveCurrentUser(currentUser)
    }
}