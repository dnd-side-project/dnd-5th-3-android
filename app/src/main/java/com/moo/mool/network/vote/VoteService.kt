package com.moo.mool.network.vote

import com.moo.mool.view.comment.model.ResponseComment
import com.moo.mool.view.vote.model.*

interface VoteService {
    suspend fun requestVoteFeedDetail(id: Int): ResponseFeedDetail

    suspend fun requestVoteFeedComment(id: Int): ResponseComment

    suspend fun requestVoteFeedDelete(id: Int): ResponseId

    suspend fun requestVote(id: Int, body: RequestVote): ResponseId

    suspend fun requestVotePostComment(body: RequestPostComment): ResponsePostComment
}
