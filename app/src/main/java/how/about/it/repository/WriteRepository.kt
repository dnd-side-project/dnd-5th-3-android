package how.about.it.repository

import android.content.Context
import how.about.it.model.RequestUploadPost
import how.about.it.model.ResponseUploadPost
import how.about.it.network.RequestToServer
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response

class WriteRepository(private val context: Context) {
    interface WriteCallBack {
        fun onSuccess()
        fun onError(message: String?)
    }

    fun uploadPost(title: String, content: String, productImageUrl: String?, writeCallBack: WriteCallBack) {
        RequestToServer.initAccessToken(context)
        RequestToServer.service.requestUploadPost(
            RequestUploadPost(title, content, productImageUrl)
        ).enqueue(object : Callback<ResponseUploadPost> {
            override fun onFailure(call: retrofit2.Call<ResponseUploadPost>, t: Throwable) {
                writeCallBack.onError(t.localizedMessage)
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseUploadPost>,
                response: Response<ResponseUploadPost>
            ) {
                if (response.isSuccessful) {
                    writeCallBack.onSuccess()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        writeCallBack.onError(jObjError.getString("user_msg"))
                    } catch (e: Exception) {
                        writeCallBack.onError(e.message)
                    }
                }
            }
        })
    }
}