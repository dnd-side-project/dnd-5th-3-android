package com.moo.mool.network.mypage

import com.moo.mool.view.feed.model.ResponseMainFeed

interface MyPageService {
    suspend fun requestMyPageFeedList(sorted: String): ResponseMainFeed
}
