package how.about.it.view.comment.repository

import how.about.it.network.comment.CommentServiceImpl
import how.about.it.view.comment.RequestPostEmoji
import how.about.it.view.comment.RequestPostReComment
import how.about.it.view.comment.RequestPutEmoji
import how.about.it.view.commentupdate.RequestPutComment
import how.about.it.view.vote.RequestCommentId
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