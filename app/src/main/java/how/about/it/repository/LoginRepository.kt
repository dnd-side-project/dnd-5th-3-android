package how.about.it.repository

import android.content.Context
import how.about.it.database.SharedManager
import how.about.it.database.User
import how.about.it.model.RequestLogin
import how.about.it.model.ResponseLogin
import how.about.it.network.RequestToServer
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val context: Context) {
    private val sharedManager : SharedManager by lazy { SharedManager(context) }

    interface LoginCallBack {
        fun onSuccess()
        fun onError(message: String?)
    }

    fun loginUser(requestLogin: RequestLogin, loginCallBack: LoginCallBack) {
        RequestToServer.service.requestLogin(
            RequestLogin(
                requestLogin.userId,
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