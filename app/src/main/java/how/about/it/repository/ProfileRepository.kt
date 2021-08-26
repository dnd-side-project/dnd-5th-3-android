package how.about.it.repository

import android.content.Context
import how.about.it.database.SharedManager
import how.about.it.model.RequestOldPasswordCheck
import how.about.it.model.RequestProfileUpdate
import how.about.it.model.ResponseMember
import how.about.it.network.RequestToServer
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepository(private val context: Context) {
    private val sharedManager : SharedManager by lazy { SharedManager(context) }
    interface ProfileCallBack {
        fun onSuccess()
        fun onError(message: String?)
    }
    fun duplicateCheckNickname(nickname: String, profileCallBack: ProfileCallBack) {
        RequestToServer.service.requestDuplicateCheckNickname(nickname)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    profileCallBack.onError(t.localizedMessage)
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful && response.body().toString() == "false") {
                        profileCallBack.onSuccess()
                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            profileCallBack.onError(jObjError.getString("user_msg"))
                        } catch (e: Exception) {
                            profileCallBack.onError(e.message)
                        }
                    }
                }
            })
    }

    fun updateNickname(nickname : String, profileCallBack: ProfileCallBack) {
        RequestToServer.service.requestProfileUpdate(
            sharedManager.getCurrentUser().accessToken.toString(), RequestProfileUpdate(nickname)
        ).enqueue(object : Callback<ResponseMember> {
            override fun onResponse(
                call: Call<ResponseMember>,
                response: Response<ResponseMember>
            ) {
                if (response.isSuccessful) {
                    profileCallBack.onSuccess()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        profileCallBack.onError(jObjError.getString("user_msg"))
                    } catch (e: Exception) {
                        profileCallBack.onError(e.message)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseMember>, t: Throwable) {
                profileCallBack.onError(t.localizedMessage)
            }
        })
    }

    fun updatePassword(newPassword : String, profileCallBack: ProfileCallBack) {
        RequestToServer.service.requestProfileUpdate(
            sharedManager.getCurrentUser().accessToken.toString(), RequestProfileUpdate(null, newPassword)
        ).enqueue(object : Callback<ResponseMember> {
            override fun onResponse(
                call: Call<ResponseMember>,
                response: Response<ResponseMember>
            ) {
                if (response.isSuccessful) {
                    profileCallBack.onSuccess()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        profileCallBack.onError(jObjError.getString("user_msg"))
                    } catch (e: Exception) {
                        profileCallBack.onError(e.message)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseMember>, t: Throwable) {
                profileCallBack.onError(t.localizedMessage)
            }
        })
    }

    fun checkOldPassword(oldPassword : String, profileCallBack: ProfileCallBack) {
        RequestToServer.service.requestCheckOldPassword(
            sharedManager.getCurrentUser().accessToken.toString(), RequestOldPasswordCheck(oldPassword)
        ).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful && response.body().toString() == "true") { // 현재 비밀번호와 동일 할경우 true
                    profileCallBack.onSuccess()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        profileCallBack.onError(jObjError.getString("user_msg"))
                    } catch (e: Exception) {
                        profileCallBack.onError(e.message)
                    }
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                profileCallBack.onError(t.localizedMessage)
            }
        })
    }

    fun checkNewPassword(newPassword : String, profileCallBack: ProfileCallBack) {
        RequestToServer.service.requestCheckOldPassword(
            sharedManager.getCurrentUser().accessToken.toString(), RequestOldPasswordCheck(newPassword)
        ).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful && response.body().toString() == "false") { // 현재 비밀번호와 동일 할경우 true
                    profileCallBack.onSuccess()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        profileCallBack.onError(jObjError.getString("user_msg"))
                    } catch (e: Exception) {
                        profileCallBack.onError(e.message)
                    }
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                profileCallBack.onError(t.localizedMessage)
            }
        })
    }

}