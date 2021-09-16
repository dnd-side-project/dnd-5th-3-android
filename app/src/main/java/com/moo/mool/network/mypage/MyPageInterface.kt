package com.moo.mool.network.mypage

import com.moo.mool.view.feed.model.ResponseMainFeed
import retrofit2.http.GET
import retrofit2.http.Query

interface MyPageInterface {
    @GET("/api/v1/mypage")
    suspend fun requestMyPageFeedList(
        @Query("sorted") sorted: String
    ): ResponseMainFeed
}
