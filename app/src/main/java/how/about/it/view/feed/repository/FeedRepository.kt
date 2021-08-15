package how.about.it.view.feed.repository

import how.about.it.network.feed.FeedServiceImpl

class FeedRepository(private val feedServiceImpl: FeedServiceImpl) {
    suspend fun requestTopFeedList() = feedServiceImpl.requestTopFeedList()

    suspend fun requestBottomFeedList(sorted: String) =
        feedServiceImpl.requestBottomFeedList(sorted)
}
