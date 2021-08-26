package how.about.it.network.mypage

import javax.inject.Inject

class MyPageServiceImpl @Inject constructor(
    private val myPageInterface: MyPageInterface
) : MyPageService {
    override suspend fun requestMyPageFeedList(sorted: String) =
        myPageInterface.requestMyPageFeedList(sorted)
}
