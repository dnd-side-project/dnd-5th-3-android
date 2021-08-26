package how.about.it.network.mypage

import how.about.it.view.feed.ResponseMainFeed

interface MyPageService {
    suspend fun requestMyPageFeedList(sorted: String): ResponseMainFeed
}
