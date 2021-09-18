package com.moo.mool.network.comment

import com.moo.mool.view.comment.model.RequestPostReComment
import com.moo.mool.view.comment.model.ResponseComment
import com.moo.mool.view.commentupdate.model.RequestPutComment
import com.moo.mool.view.vote.model.RequestCommentId
import com.moo.mool.view.vote.model.ResponsePostComment

interface CommentService {
    suspend fun requestGetReply(id: Int): ResponseComment

    suspend fun requestPostReply(id: Int, body: RequestPostReComment): ResponsePostComment

    suspend fun requestUpdateComment(body: RequestPutComment): ResponsePostComment

    suspend fun requestDeleteComment(body: RequestCommentId): ResponsePostComment
}
