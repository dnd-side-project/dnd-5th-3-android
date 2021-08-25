package how.about.it.network.vote

import how.about.it.view.comment.RequestPutEmoji
import how.about.it.view.comment.ResponseComment
import how.about.it.view.comment.ResponseEmoji
import how.about.it.view.vote.*

interface VoteService {
    suspend fun requestVoteFeedDetail(id: Int): ResponseFeedDetail

    suspend fun requestVoteFeedComment(id: Int): ResponseComment

    suspend fun requestVote(id: Int, body: RequestVote): ResponseId

    suspend fun requestVoteDelete(id: Int): ResponseId

    suspend fun requestVotePostComment(body: RequestPostComment): ResponsePostComment

    suspend fun requestCommentDelete(body: RequestCommentId): ResponsePostComment

    suspend fun requestPutEmoji(body: RequestPutEmoji): ResponseEmoji
}
