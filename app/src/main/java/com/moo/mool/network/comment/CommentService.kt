package com.moo.mool.network.comment

import com.moo.mool.view.comment.*
import com.moo.mool.view.commentupdate.RequestPutComment
import com.moo.mool.view.vote.RequestCommentId
import com.moo.mool.view.vote.ResponsePostComment

interface CommentService {
    suspend fun requestCommentReply(id: Int): ResponseComment

    suspend fun requestPostReComment(id: Int, body: RequestPostReComment): ResponsePostComment

    suspend fun requestCommentUpdate(body: RequestPutComment): ResponsePostComment

    suspend fun requestCommentDelete(body: RequestCommentId): ResponsePostComment

    suspend fun requestPostEmoji(body: RequestPostEmoji): ResponseEmoji

    suspend fun requestPutEmoji(body: RequestPutEmoji): ResponseEmoji
}