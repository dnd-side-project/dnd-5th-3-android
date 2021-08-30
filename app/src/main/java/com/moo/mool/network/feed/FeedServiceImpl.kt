package com.moo.mool.network.feed

import javax.inject.Inject

class FeedServiceImpl @Inject constructor(
    private val feedInterface: FeedInterface
) : FeedService {
    override suspend fun requestTopFeedList() = feedInterface.requestTopFeedList()

    override suspend fun requestBottomFeedList(sorted: String) =
        feedInterface.requestBottomFeedList(sorted)
}