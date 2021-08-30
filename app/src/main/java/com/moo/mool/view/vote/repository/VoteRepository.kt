package com.moo.mool.view.vote.repository

import com.moo.mool.network.vote.VoteServiceImpl
import com.moo.mool.view.comment.RequestPutEmoji
import com.moo.mool.view.vote.RequestCommentId
import com.moo.mool.view.vote.RequestPostComment
import com.moo.mool.view.vote.RequestVote
import javax.inject.Inject

class VoteRepository @Inject constructor(
    private val voteServiceImpl: VoteServiceImpl
) {
    suspend fun requestVoteFeedDetail(id: Int) = voteServiceImpl.requestVoteFeedDetail(id)

    suspend fun requestVoteFeedComment(id: Int) = voteServiceImpl.requestVoteFeedComment(id)

    suspend fun requestVote(id: Int, body: RequestVote) = voteServiceImpl.requestVote(id, body)

    suspend fun requestVoteDelete(id: Int) = voteServiceImpl.requestVoteDelete(id)

    suspend fun requestVotePostComment(body: RequestPostComment) =
        voteServiceImpl.requestVotePostComment(body)

    suspend fun requestCommentDelete(body: RequestCommentId) =
        voteServiceImpl.requestCommentDelete(body)

    suspend fun requestPutEmoji(body: RequestPutEmoji) =
        voteServiceImpl.requestPutEmoji(body)
}
