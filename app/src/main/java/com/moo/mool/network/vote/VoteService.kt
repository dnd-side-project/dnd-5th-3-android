package com.moo.mool.network.vote

import com.moo.mool.view.comment.model.RequestPutEmoji
import com.moo.mool.view.comment.model.ResponseComment
import com.moo.mool.view.comment.model.ResponseEmoji
import com.moo.mool.view.vote.model.*

interface VoteService {
    suspend fun requestVoteFeedDetail(id: Int): ResponseFeedDetail

    suspend fun requestVoteFeedComment(id: Int): ResponseComment

    suspend fun requestVote(id: Int, body: RequestVote): ResponseId

    suspend fun requestVoteDelete(id: Int): ResponseId

    suspend fun requestVotePostComment(body: RequestPostComment): ResponsePostComment

    suspend fun requestCommentDelete(body: RequestCommentId): ResponsePostComment

    suspend fun requestPutEmoji(body: RequestPutEmoji): ResponseEmoji
}
