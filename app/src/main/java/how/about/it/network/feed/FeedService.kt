package how.about.it.network.feed

import how.about.it.view.feed.ResponseMainFeed

interface FeedService {
    suspend fun requestTopFeedList(): ResponseMainFeed

    suspend fun requestBottomFeedList(sorted: String): ResponseMainFeed
}
