package how.about.it.network.comment

import how.about.it.view.comment.*
import how.about.it.view.commentupdate.RequestPutComment
import how.about.it.view.vote.RequestCommentId
import how.about.it.view.vote.ResponsePostComment

interface CommentService {
    suspend fun requestCommentReply(id: Int): ResponseComment

    suspend fun requestPostReComment(id: Int, body: RequestPostReComment): ResponsePostComment

    suspend fun requestCommentUpdate(body: RequestPutComment): ResponsePostComment

    suspend fun requestCommentDelete(body: RequestCommentId): ResponsePostComment

    suspend fun requestPostEmoji(body: RequestPostEmoji): ResponseEmoji

    suspend fun requestPutEmoji(body: RequestPutEmoji): ResponseEmoji
}