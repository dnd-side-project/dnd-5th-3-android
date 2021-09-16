package com.moo.mool.view.feed.repository

import com.moo.mool.network.feed.FeedServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val feedServiceImpl: FeedServiceImpl
) {
    suspend fun requestTopFeedList() = flow {
        runCatching {
            feedServiceImpl.requestTopFeedList()
        }.getOrNull()?.let { feedList ->
            emit(feedList.posts.filter { feed ->
                (feed.id != -1)
            })
        } ?: emit(null)
    }.flowOn(Dispatchers.IO)

    suspend fun requestBottomFeedList(sorted: String) = flow {
        runCatching {
            feedServiceImpl.requestBottomFeedList(sorted)
        }.getOrNull()?.let { feedList ->
            emit(feedList.posts)
        } ?: emit(null)
    }.flowOn(Dispatchers.IO)
}
