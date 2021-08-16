package how.about.it.network.comment

class CommentServiceImpl(private val commentInterface: CommentInterface) : CommentService {
    override suspend fun requestCommentReply(id: Int) = commentInterface.requestCommentReply(id)
}