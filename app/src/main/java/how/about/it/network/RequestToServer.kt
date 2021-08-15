package how.about.it.network

import how.about.it.network.feed.FeedInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestToServer {
    private var retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-3-34-111-109.ap-northeast-2.compute.amazonaws.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: RequestInterface = retrofit.create(
        RequestInterface::class.java
    )
    val feedInterface: FeedInterface = retrofit.create(FeedInterface::class.java)
}