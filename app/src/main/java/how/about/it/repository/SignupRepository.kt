package how.about.it.repository

import how.about.it.model.RequestMember
import how.about.it.model.ResponseMember
import how.about.it.network.RequestToServer
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupRepository() {
    interface SignupCallBack {
        fun onSuccess()
        fun onError(message: String?)
    }

    fun duplicateCheckEmail(email: String, duplicateCheckEmailCallBack: SignupCallBack) {
        RequestToServer.service.requestDuplicateCheckEmail(email)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    duplicateCheckEmailCallBack.onError(t.localizedMessage)
                }
                override fun onResponse(call: Call<String>, response: Response<String>){
                    if (response.isSuccessful && response.body().toString() == "false") {
                        duplicateCheckEmailCallBack.onSuccess()
                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            duplicateCheckEmailCallBack.onError(jObjError.getString("user_msg"))
                        } catch (e: Exception) {
                            duplicateCheckEmailCallBack.onError(e.message)
                        }
                    }
                }
            })
    }

    fun duplicateCheckNickname(nickname: String, duplicateCheckNicknameCallBack: SignupCallBack) {
        RequestToServer.service.requestDuplicateCheckNickname(nickname)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    duplicateCheckNicknameCallBack.onError(t.localizedMessage)
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful && response.body().toString() == "false") {
                        duplicateCheckNicknameCallBack.onSuccess()
                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            duplicateCheckNicknameCallBack.onError(jObjError.getString("user_msg"))
                        } catch (e: Exception) {
                            duplicateCheckNicknameCallBack.onError(e.message)
                        }
                    }
                }
            })
    }

    fun signupUser(requestMember: RequestMember, signupCallBack: SignupCallBack) {
        RequestToServer.service.requestMember(
            RequestMember(
                requestMember.email,
                requestMember.password,
                requestMember.nickname
            )   //회원가입 정보를 전달
        ).enqueue(object : Callback<ResponseMember> {
            override fun onFailure(call: retrofit2.Call<ResponseMember>, t: Throwable) {
                signupCallBack.onError(t.localizedMessage)
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseMember>,
                response: Response<ResponseMember>
            ) {
                if (response.isSuccessful) {
                    signupCallBack.onSuccess()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        signupCallBack.onError(jObjError.getString("user_msg"))
                    } catch (e: Exception) {
                        signupCallBack.onError(e.message)
                    }
                }
            }
        })
    }
}