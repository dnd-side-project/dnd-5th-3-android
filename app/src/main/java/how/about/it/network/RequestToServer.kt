package how.about.it.network

import android.content.Context
import how.about.it.database.SharedManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import how.about.it.network.feed.FeedInterface
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RequestToServer {
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private lateinit var sharedManager : SharedManager
    private lateinit var accessToken : String

    fun initAccessToken(context: Context){
        sharedManager = SharedManager(context)
        accessToken = sharedManager.getCurrentUser().accessToken.toString()
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            // Header에 AccessToken을 삽입하지 않는 대상
            if (request.url.encodedPath.equals("/api/v1/login", true)
                || request.url.encodedPath.equals("/api/v1/member", true)
                || request.url.encodedPath.equals("/api/v1/member/token", true)) {
                chain.proceed(request)
            } else {
                chain.proceed(request.newBuilder().apply {
                    addHeader("Authorization", accessToken)
                }.build())
            }
        }
        .addInterceptor(logger)
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