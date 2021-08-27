package how.about.it.repository

import android.content.Context
import how.about.it.database.SharedManager
import how.about.it.model.RequestDeleteAccount
import how.about.it.model.ResponseMember
import how.about.it.network.RequestToServer
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingRepository(private val context: Context) {
    private val sharedManager : SharedManager by lazy { SharedManager(context) }
    interface SettingCallBack {
        fun onSuccess()
        fun onError(message: String?)
    }

    fun deleteAccount(settingCallBack : SettingCallBack) {
        RequestToServer.service.requestDeleteAccount(
            sharedManager.getCurrentUser().accessToken.toString(), RequestDeleteAccount(sharedManager.getCurrentUser().email.toString())
        ).enqueue(object : Callback<ResponseMember> {
                override fun onFailure(call: Call<ResponseMember>, t: Throwable) {
                    settingCallBack.onError(t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<ResponseMember>,
                    response: Response<ResponseMember>
                ) {
                    if (response.isSuccessful && response.body().toString() == "false") {
                        settingCallBack.onSuccess()
                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            settingCallBack.onError(jObjError.getString("user_msg"))
                        } catch (e: Exception) {
                            settingCallBack.onError(e.message)
                        }
                    }
                }
        })
    }
}