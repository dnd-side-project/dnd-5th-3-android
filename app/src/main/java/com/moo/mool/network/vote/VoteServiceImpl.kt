package com.moo.mool.network.vote

import com.moo.mool.view.comment.model.RequestPutEmoji
import com.moo.mool.view.vote.model.RequestCommentId
import com.moo.mool.view.vote.model.RequestPostComment
import com.moo.mool.view.vote.model.RequestVote
import javax.inject.Inject

class VoteServiceImpl @Inject constructor(
    private val voteInterface: VoteInterface
) : VoteService {
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
