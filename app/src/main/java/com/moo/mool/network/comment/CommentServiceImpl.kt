package com.moo.mool.network.comment

import com.moo.mool.view.comment.model.RequestPostEmoji
import com.moo.mool.view.comment.model.RequestPostReComment
import com.moo.mool.view.comment.model.RequestPutEmoji
import com.moo.mool.view.commentupdate.model.RequestPutComment
import com.moo.mool.view.vote.model.RequestCommentId
import javax.inject.Inject

class CommentServiceImpl @Inject constructor(
    private val commentInterface: CommentInterface
) : CommentService {
    override suspend fun requestCommentReply(id: Int) = commentInterface.requestCommentReply(id)

    override suspend fun requestPostReComment(id: Int, body: RequestPostReComment) =
        commentInterface.requestPostReply(id, body)

    override suspend fun requestCommentUpdate(body: RequestPutComment) =
        commentInterface.requestCommentUpdate(body)

    override suspend fun requestCommentDelete(body: RequestCommentId) =
        commentInterface.requestCommentDelete(body)

    override suspend fun requestPostEmoji(body: RequestPostEmoji) =
        commentInterface.requestPostEmoji(body)

    override suspend fun requestPutEmoji(body: RequestPutEmoji) =
        commentInterface.requestPutEmoji(body)
}