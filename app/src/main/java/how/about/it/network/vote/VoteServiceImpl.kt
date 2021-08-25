package how.about.it.network.vote

import how.about.it.view.comment.RequestPutEmoji
import how.about.it.view.vote.RequestCommentId
import how.about.it.view.vote.RequestPostComment
import how.about.it.view.vote.RequestVote

class VoteServiceImpl(private val voteInterface: VoteInterface) : VoteService {
    override suspend fun requestVoteFeedDetail(id: Int) = voteInterface.requestVoteFeedDetail(id)

    override suspend fun requestVoteFeedComment(id: Int) = voteInterface.requestVoteFeedComment(id)

    override suspend fun requestVote(id: Int, body: RequestVote) =
        voteInterface.requestVote(id, body)

    override suspend fun requestVoteDelete(id: Int) = voteInterface.requestVoteDelete(id)

    override suspend fun requestVotePostComment(body: RequestPostComment) =
        voteInterface.requestVotePostComment(body)

    override suspend fun requestCommentDelete(body: RequestCommentId) =
        voteInterface.requestCommentDelete(body)

    override suspend fun requestPutEmoji(body: RequestPutEmoji) =
        voteInterface.requestPutEmoji(body)
}
