package how.about.it.network

import how.about.it.model.RequestLogin
import how.about.it.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestInterface {
    @POST("/api/login")
    fun requestLogin(@Body body : RequestLogin) : Call<ResponseLogin>
}