package how.about.it.network.comment

import how.about.it.view.comment.ResponseComment
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentInterface {
    @GET("/api/v1/comment/{id}")
    suspend fun requestCommentReply(
        @Path("id") id: Int,
    ): ResponseComment

}