package how.about.it.network

import how.about.it.model.RequestLogin
import how.about.it.model.RequestMember
import how.about.it.model.ResponseLogin
import how.about.it.model.ResponseMember
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestInterface {
    @POST("/api/v1/login")
    fun requestLogin(@Body body : RequestLogin) : Call<ResponseLogin>

    @POST("/api/v1/member")
    fun requestMember(@Body body : RequestMember) : Call<ResponseMember>
}