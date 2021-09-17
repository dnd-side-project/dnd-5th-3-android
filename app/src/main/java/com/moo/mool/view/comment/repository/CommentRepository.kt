package com.moo.mool.view.comment.repository

import com.moo.mool.network.comment.CommentServiceImpl
import com.moo.mool.view.comment.model.RequestPostEmoji
import com.moo.mool.view.comment.model.RequestPostReComment
import com.moo.mool.view.comment.model.RequestPutEmoji
import com.moo.mool.view.commentupdate.model.RequestPutComment
import com.moo.mool.view.vote.model.RequestCommentId
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentServiceImpl: CommentServiceImpl
) {
    suspend fun requestGetReply(id: Int) = flow {
        runCatching {
            commentServiceImpl.requestGetReply(id)
        }.getOrNull()?.let { commentReply ->
            emit(commentReply.commentList.filterIndexed { index, comment ->
                (index == 0 || !comment.deleted)
            })
        } ?: emit(null)
    }

    suspend fun requestPostReply(id: Int, content: String) = flow {
        runCatching {
            commentServiceImpl.requestPostReply(id, RequestPostReComment(content = content))
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(true)
    }

    suspend fun requestUpdateComment(id: Int, content: String) = flow {
        runCatching {
            commentServiceImpl.requestUpdateComment(
                RequestPutComment(
                    commentId = id,
                    content = content
                )
            )
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(false)
    }

    suspend fun requestDeleteComment(id: Int) = flow {
        runCatching {
            commentServiceImpl.requestDeleteComment(RequestCommentId(id))
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(false)
    }

    suspend fun requestPostEmoji(body: RequestPostEmoji) =
        commentServiceImpl.requestPostEmoji(body)

    suspend fun requestPutEmoji(body: RequestPutEmoji) =
        commentServiceImpl.requestPutEmoji(body)
}
