package how.about.it.network.comment

import how.about.it.view.comment.ResponseComment

interface CommentService {
    suspend fun requestCommentReply(id: Int): ResponseComment
}