package how.about.it.network.comment

import how.about.it.view.comment.RequestPostEmoji
import how.about.it.view.comment.RequestPostReComment
import how.about.it.view.comment.RequestPutEmoji
import how.about.it.view.commentupdate.RequestPutComment
import how.about.it.view.vote.RequestCommentId
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