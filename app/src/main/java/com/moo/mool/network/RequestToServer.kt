package com.moo.mool.network

import android.content.Context
import com.moo.mool.database.SharedManager
import com.moo.mool.network.comment.CommentInterface
import com.moo.mool.network.emoji.EmojiInterface
import com.moo.mool.network.feed.FeedInterface
import com.moo.mool.network.login.LoginInterface
import com.moo.mool.network.mypage.MyPageInterface
import com.moo.mool.network.vote.VoteInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RequestToServer {
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private lateinit var sharedManager: SharedManager
    private lateinit var accessToken: String

    fun initAccessToken(context: Context) {
        sharedManager = SharedManager(context)
        accessToken = sharedManager.getCurrentUser().accessToken.toString()
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            // Header에 AccessToken을 삽입하지 않는 대상
            if (request.url.encodedPath.equals("/api/v1/login", true)
                || request.url.encodedPath.startsWith("/api/v1/member", true)
            ) {
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
        .addConverterFactory(ScalarsConverterFactory.create()) // this line
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    var service: RequestInterface = retrofit.create(
        RequestInterface::class.java
    )
    val feedInterface: FeedInterface = retrofit.create(FeedInterface::class.java)
    val voteInterface: VoteInterface = retrofit.create(VoteInterface::class.java)
    val commentInterface: CommentInterface = retrofit.create(CommentInterface::class.java)
    val emojiInterface: EmojiInterface = retrofit.create(EmojiInterface::class.java)
    val myPageInterface: MyPageInterface = retrofit.create(MyPageInterface::class.java)
    val loginInterface: LoginInterface = retrofit.create(LoginInterface::class.java)
}
