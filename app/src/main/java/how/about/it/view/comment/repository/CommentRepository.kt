package how.about.it.view.comment.repository

import how.about.it.network.comment.CommentServiceImpl
import how.about.it.view.comment.RequestPostReComment
import how.about.it.view.commentupdate.RequestPutComment

class CommentRepository(private val commentServiceImpl: CommentServiceImpl) {
    suspend fun requestCommentReply(id: Int) = commentServiceImpl.requestCommentReply(id)

    suspend fun requestPostReComment(id: Int, body: RequestPostReComment) =
        commentServiceImpl.requestPostReComment(id, body)

    suspend fun requestCommentUpdate(body: RequestPutComment) =
        commentServiceImpl.requestCommentUpdate(body)
}