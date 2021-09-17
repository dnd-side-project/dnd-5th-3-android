package com.moo.mool.network.comment

import com.moo.mool.view.comment.model.*
import com.moo.mool.view.commentupdate.model.RequestPutComment
import com.moo.mool.view.vote.model.RequestCommentId
import com.moo.mool.view.vote.model.ResponsePostComment

interface CommentService {
    suspend fun requestGetReply(id: Int): ResponseComment

    suspend fun requestPostReply(id: Int, body: RequestPostReComment): ResponsePostComment

    suspend fun requestUpdateComment(body: RequestPutComment): ResponsePostComment

    suspend fun requestDeleteComment(body: RequestCommentId): ResponsePostComment

    suspend fun requestPostEmoji(body: RequestPostEmoji): ResponseEmoji

    suspend fun requestPutEmoji(body: RequestPutEmoji): ResponseEmoji
}
