package how.about.it.network.comment

import how.about.it.view.comment.RequestPostReComment
import how.about.it.view.comment.ResponseComment
import how.about.it.view.commentupdate.RequestPutComment
import how.about.it.view.vote.ResponsePostComment
import retrofit2.http.*

interface CommentInterface {
    @GET("/api/v1/comment/{id}")
    suspend fun requestCommentReply(
        @Path("id") id: Int,
    ): ResponseComment

    @POST("/api/v1/comment/{commentId}/reply")
    suspend fun requestPostReply(
        @Path("commentId") commentId: Int,
        @Body body: RequestPostReComment
    ): ResponsePostComment

    @PUT("/api/v1/comment")
    suspend fun requestCommentUpdate(
        @Body body: RequestPutComment,
    ): ResponsePostComment
}