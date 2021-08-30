package com.moo.mool.repository

import android.content.Context
import com.moo.mool.database.SharedManager
import com.moo.mool.database.User
import com.moo.mool.model.RequestLogin
import com.moo.mool.model.RequestTokenRefresh
import com.moo.mool.model.ResponseLogin
import com.moo.mool.model.ResponseTokenRefresh
import com.moo.mool.network.RequestToServer
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val context: Context) {
    private val sharedManager : SharedManager by lazy { SharedManager(context) }

    interface LoginCallBack {
        fun onSuccess()
        fun onError(message: String?)
    }

    fun autoLogin(loginCallBack: LoginCallBack) {
        if(sharedManager.getRefreshToken().isNotEmpty()){
            sharedManager.setSkipOnBoardingFragment() // 자동 로그인시 온보딩페이지 SKIP할수 있도록 설정
            // refreshToken 정보가 있는 경우
            val currentUser = sharedManager.getCurrentUser()
            RequestToServer.service.requestTokenRefresh(
                RequestTokenRefresh(
                    currentUser.email.toString(),
                    currentUser.refreshToken.toString())
            ).enqueue(object : Callback<ResponseTokenRefresh> {
                override fun onResponse(
                    call: Call<ResponseTokenRefresh>,
                    response: Response<ResponseTokenRefresh>
                ) {
                    if (response.isSuccessful) {
                        // TokenRefresh 성공 후 새로운 AccessToken 저장
                        sharedManager.updateAccessToken(response.body()!!.accessToken)
                        loginCallBack.onSuccess()
                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            loginCallBack.onError(jObjError.getString("user_msg"))
                        } catch (e: Exception) {
                            loginCallBack.onError(e.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseTokenRefresh>, t: Throwable) {
                    loginCallBack.onError(t.localizedMessage)
                }
            })
        } else { // 로그인 정보 미존재시, Error 처리하여 ViewModel에 전달
            loginCallBack.onError(null)
        }
    }

    fun loginUser(requestLogin: RequestLogin, loginCallBack: LoginCallBack) {
        RequestToServer.service.requestLogin(
            RequestLogin(
                requestLogin.email,
                requestLogin.password
            )   //로그인 정보를 전달
        ).enqueue(object : Callback<ResponseLogin> {
            override fun onFailure(call: retrofit2.Call<ResponseLogin>, t: Throwable) {
                loginCallBack.onError(t.localizedMessage)
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseLogin>,
                response: Response<ResponseLogin>
            ) {
                if (response.isSuccessful) {
                    // 로그인 성공 후 토큰 저장
                    val currentUser = User().apply {
                        accessToken = response.body()!!.accessToken
                        refreshToken = response.body()!!.refreshToken
                        nickname = response.body()!!.name
                        email = response.body()!!.email
                    }
                    sharedManager.saveCurrentUser(currentUser) // SharedPreference에 저장
                    loginCallBack.onSuccess()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        loginCallBack.onError(jObjError.getString("user_msg"))
                    } catch (e: Exception) {
                        loginCallBack.onError(e.message)
                    }
                }
            }
        })
    }
}