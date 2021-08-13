package how.about.it.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RequestToServer {
    var retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-3-34-111-109.ap-northeast-2.compute.amazonaws.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: RequestInterface = retrofit.create(
        RequestInterface::class.java)
}