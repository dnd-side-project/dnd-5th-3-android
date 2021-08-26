package how.about.it.view.mypage.repository

import how.about.it.network.mypage.MyPageServiceImpl
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val myPageServiceImpl: MyPageServiceImpl
) {
    suspend fun requestMyPageFeedList(sorted: String) =
        myPageServiceImpl.requestMyPageFeedList(sorted)
}
