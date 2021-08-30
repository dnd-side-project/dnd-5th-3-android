package com.moo.mool.view.comment.repository

import com.moo.mool.network.comment.CommentServiceImpl
import com.moo.mool.view.comment.RequestPostEmoji
import com.moo.mool.view.comment.RequestPostReComment
import com.moo.mool.view.comment.RequestPutEmoji
import com.moo.mool.view.commentupdate.RequestPutComment
import com.moo.mool.view.vote.RequestCommentId
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentServiceImpl: CommentServiceImpl
) {
    suspend fun requestCommentReply(id: Int) = commentServiceImpl.requestCommentReply(id)

    suspend fun requestPostReComment(id: Int, body: RequestPostReComment) =
        commentServiceImpl.requestPostReComment(id, body)

    suspend fun requestCommentUpdate(body: RequestPutComment) =
        commentServiceImpl.requestCommentUpdate(body)

    suspend fun requestCommentDelete(body: RequestCommentId) =
        commentServiceImpl.requestCommentDelete(body)

    suspend fun requestPostEmoji(body: RequestPostEmoji) =
        commentServiceImpl.requestPostEmoji(body)

    suspend fun requestPutEmoji(body: RequestPutEmoji) =
        commentServiceImpl.requestPutEmoji(body)
}