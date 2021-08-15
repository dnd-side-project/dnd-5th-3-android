package how.about.it.network.feed

import how.about.it.view.feed.ResponseMainFeed
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedInterface {
    @GET("/api/v1/posts/main")
    suspend fun requestTopFeedList(): ResponseMainFeed

    @GET("/api/v1/posts")
    suspend fun requestBottomFeedList(
        @Query("sorted") sorted: String,
        @Query("offset") offset: Int = 0
    ): ResponseMainFeed
}
