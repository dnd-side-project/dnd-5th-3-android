package how.about.it.view.comment.repository

import how.about.it.network.comment.CommentServiceImpl

class CommentRepository(private val commentServiceImpl: CommentServiceImpl) {
    suspend fun requestCommentReply(id: Int) = commentServiceImpl.requestCommentReply(id)
}