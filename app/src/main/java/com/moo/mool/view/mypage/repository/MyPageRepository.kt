package com.moo.mool.view.mypage.repository

import com.moo.mool.network.mypage.MyPageServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val myPageServiceImpl: MyPageServiceImpl
) {
    suspend fun requestMyPageFeedList(sorted: String) = flow {
        runCatching {
            myPageServiceImpl.requestMyPageFeedList(sorted)
        }.getOrNull()?.let { feedList ->
            emit(feedList.posts)
        } ?: emit(null)
    }.flowOn(Dispatchers.IO)
}
