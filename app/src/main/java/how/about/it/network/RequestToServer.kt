package how.about.it.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RequestToServer {
    var retrofit = Retrofit.Builder()
        .baseUrl("baseurl을 여기에 입력")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: RequestInterface = retrofit.create(
        RequestInterface::class.java)
}