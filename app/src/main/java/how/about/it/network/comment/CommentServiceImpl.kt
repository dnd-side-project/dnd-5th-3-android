package how.about.it.network.comment

import how.about.it.view.comment.RequestPostReComment
import how.about.it.view.commentupdate.RequestPutComment
import how.about.it.view.vote.RequestCommentId

class CommentServiceImpl(private val commentInterface: CommentInterface) : CommentService {
    override suspend fun requestCommentReply(id: Int) = commentInterface.requestCommentReply(id)

    override suspend fun requestPostReComment(id: Int, body: RequestPostReComment) =
        commentInterface.requestPostReply(id, body)

    override suspend fun requestCommentUpdate(body: RequestPutComment) =
        commentInterface.requestCommentUpdate(body)

    override suspend fun requestCommentDelete(body: RequestCommentId) =
        commentInterface.requestCommentDelete(body)
}