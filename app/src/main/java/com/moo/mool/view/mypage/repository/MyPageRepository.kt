package com.moo.mool.view.mypage.repository

import com.moo.mool.network.mypage.MyPageServiceImpl
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val myPageServiceImpl: MyPageServiceImpl
) {
    suspend fun requestMyPageFeedList(sorted: String) =
        myPageServiceImpl.requestMyPageFeedList(sorted)
}
