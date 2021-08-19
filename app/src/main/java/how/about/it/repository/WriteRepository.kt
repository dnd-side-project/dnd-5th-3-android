package how.about.it.repository

import android.content.Context
import how.about.it.model.ResponseUploadPost
import how.about.it.network.RequestToServer
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class WriteRepository(private val context: Context) {
    interface WriteCallBack {
        fun onSuccess()
        fun onError(message: String?)
    }

    fun uploadPost(title: String, content: String, file: File?, writeCallBack: WriteCallBack) {
        var uploadFile : MultipartBody.Part?= null
        if(file != null) {
            val fileNameDivideList: List<String> = file.toString().split("/")
            var requestBodyFile : RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file!!)
            uploadFile = MultipartBody.Part.createFormData("file",fileNameDivideList[fileNameDivideList.size - 1], requestBodyFile)
        } else if(file == null){
            var requestBodyEmpty : RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), "")
            uploadFile = MultipartBody.Part.createFormData("file", "", requestBodyEmpty)
        }
        RequestToServer.initAccessToken(context)
        RequestToServer.service.requestUploadPost(
            title, content, uploadFile
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