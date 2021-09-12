package com.moo.mool.repository

import android.content.Context
import com.moo.mool.database.SharedManager
import com.moo.mool.model.RequestDeleteAccount
import com.moo.mool.model.ResponseMember
import com.moo.mool.network.RequestToServer
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SettingRepository @Inject constructor(@ApplicationContext val context: Context) {
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

    fun setHideEmojiMotion() = sharedManager.setHideEmojiMotion()
    fun setShowEmojiMotion() = sharedManager.setShowEmojiMotion()
    val isHideEmojiMotion = sharedManager.isSkipEmojiMotion()
}