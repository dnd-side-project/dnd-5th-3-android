package how.about.it.repository

import how.about.it.network.RequestToServer
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepository() {
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

}