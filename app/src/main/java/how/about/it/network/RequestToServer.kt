package how.about.it.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import how.about.it.network.feed.FeedInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RequestToServer {
   private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
   private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(20000L, TimeUnit.SECONDS)
        .build()

   private var retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-3-34-111-109.ap-northeast-2.compute.amazonaws.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    var service: RequestInterface = retrofit.create(
        RequestInterface::class.java
    )
    val feedInterface: FeedInterface = retrofit.create(FeedInterface::class.java)
}