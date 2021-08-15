package how.about.it.network

import how.about.it.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface RequestInterface {
    @POST("/api/v1/login")
    fun requestLogin(@Body body : RequestLogin) : Call<ResponseLogin>

    @POST("/api/v1/member")
    fun requestMember(@Body body : RequestMember) : Call<ResponseMember>

    @PUT("/api/v1/member/token")
    fun requestTokenRefresh(@Body body : RequestTokenRefresh) : Call<ResponseTokenRefresh>
}