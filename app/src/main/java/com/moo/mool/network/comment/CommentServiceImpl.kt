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
    override suspend fun requestGetReply(id: Int) = commentInterface.requestGetReply(id)

    override suspend fun requestPostReply(id: Int, body: RequestPostReComment) =
        commentInterface.requestPostReply(id, body)

    override suspend fun requestUpdateComment(body: RequestPutComment) =
        commentInterface.requestUpdateComment(body)

    override suspend fun requestDeleteComment(body: RequestCommentId) =
        commentInterface.requestDeleteComment(body)

    override suspend fun requestPostEmoji(body: RequestPostEmoji) =
        commentInterface.requestPostEmoji(body)

    override suspend fun requestPutEmoji(body: RequestPutEmoji) =
        commentInterface.requestPutEmoji(body)
}
