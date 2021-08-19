package how.about.it.network

import how.about.it.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {
    @POST("/api/v1/login")
    fun requestLogin(@Body body : RequestLogin) : Call<ResponseLogin>

    @POST("/api/v1/member")
    fun requestMember(@Body body : RequestMember) : Call<ResponseMember>

    @PUT("/api/v1/member/token")
    fun requestTokenRefresh(@Body body : RequestTokenRefresh) : Call<ResponseTokenRefresh>

    @Multipart
    @POST("/api/v1/posts")
    fun requestUploadPost(@Part("title") title: String, @Part("content") content: String, @Part file: MultipartBody.Part?) : Call<ResponseUploadPost>

    @GET(" /api/v1/member/exists/{email}/email")
    fun requestDuplicateCheckEmail(@Path("email") email : String) : Call<String>

    @GET("/api/v1/member/exists/{name}/name")
    fun requestDuplicateCheckNickname(@Path("name") name : String) : Call<String>
}