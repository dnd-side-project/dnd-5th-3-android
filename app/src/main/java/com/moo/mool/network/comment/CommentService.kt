package com.moo.mool.network.comment

import com.moo.mool.view.comment.model.*
import com.moo.mool.view.commentupdate.model.RequestPutComment
import com.moo.mool.view.vote.model.RequestCommentId
import com.moo.mool.view.vote.model.ResponsePostComment

interface CommentService {
    suspend fun requestCommentReply(id: Int): ResponseComment

    suspend fun requestPostReComment(id: Int, body: RequestPostReComment): ResponsePostComment

    suspend fun requestCommentUpdate(body: RequestPutComment): ResponsePostComment

    suspend fun requestCommentDelete(body: RequestCommentId): ResponsePostComment

    suspend fun requestPostEmoji(body: RequestPostEmoji): ResponseEmoji

    suspend fun requestPutEmoji(body: RequestPutEmoji): ResponseEmoji
}