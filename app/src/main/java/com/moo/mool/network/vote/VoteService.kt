package com.moo.mool.network.vote

import com.moo.mool.view.comment.RequestPutEmoji
import com.moo.mool.view.comment.ResponseComment
import com.moo.mool.view.comment.ResponseEmoji
import com.moo.mool.view.vote.*

interface VoteService {
    suspend fun requestVoteFeedDetail(id: Int): ResponseFeedDetail

    suspend fun requestVoteFeedComment(id: Int): ResponseComment

    suspend fun requestVote(id: Int, body: RequestVote): ResponseId

    suspend fun requestVoteDelete(id: Int): ResponseId

    suspend fun requestVotePostComment(body: RequestPostComment): ResponsePostComment

    suspend fun requestCommentDelete(body: RequestCommentId): ResponsePostComment

    suspend fun requestPutEmoji(body: RequestPutEmoji): ResponseEmoji
}
