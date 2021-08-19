package how.about.it.network.comment

import how.about.it.view.commentupdate.RequestPutComment

class CommentServiceImpl(private val commentInterface: CommentInterface) : CommentService {
    override suspend fun requestCommentReply(id: Int) = commentInterface.requestCommentReply(id)

    override suspend fun requestCommentUpdate(body: RequestPutComment) =
        commentInterface.requestCommentUpdate(body)
}