package how.about.it.network.comment

import how.about.it.view.comment.ResponseComment
import how.about.it.view.commentupdate.RequestPutComment
import how.about.it.view.vote.ResponsePostComment
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CommentInterface {
    @GET("/api/v1/comment/{id}")
    suspend fun requestCommentReply(
        @Path("id") id: Int,
    ): ResponseComment

    @PUT("/api/v1/comment")
    suspend fun requestCommentUpdate(
        @Body body: RequestPutComment,
    ): ResponsePostComment
}