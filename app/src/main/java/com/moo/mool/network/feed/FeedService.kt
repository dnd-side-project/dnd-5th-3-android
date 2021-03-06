package com.moo.mool.network.feed

import com.moo.mool.view.feed.model.ResponseMainFeed

interface FeedService {
    suspend fun requestTopFeedList(): ResponseMainFeed

    suspend fun requestBottomFeedList(sorted: String): ResponseMainFeed
}
