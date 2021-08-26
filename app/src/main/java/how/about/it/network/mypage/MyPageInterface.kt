package how.about.it.network.mypage

import how.about.it.view.feed.ResponseMainFeed
import retrofit2.http.GET
import retrofit2.http.Query

interface MyPageInterface {
    @GET("/api/v1/mypage")
    suspend fun requestMyPageFeedList(
        @Query("sorted") sorted: String
    ): ResponseMainFeed
}
