package how.about.it.network.feed

class FeedServiceImpl(private val feedInterface: FeedInterface) : FeedService {
    override suspend fun requestTopFeedList() = feedInterface.requestTopFeedList()

    override suspend fun requestBottomFeedList(sorted: String) =
        feedInterface.requestBottomFeedList(sorted)
}