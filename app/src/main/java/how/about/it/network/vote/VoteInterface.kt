package how.about.it.network.vote

import how.about.it.view.comment.ResponseComment
import how.about.it.view.vote.*
import retrofit2.http.*

interface VoteInterface {
    @GET("/api/v1/posts/{id}")
    suspend fun requestVoteFeedDetail(
        @Path("id") id: Int
    ): ResponseFeedDetail

    @GET("/api/v1/comment/{id}/posts")
    suspend fun requestVoteFeedComment(
        @Path("id") id: Int,
        @Query("pageNum") pageNum: Int = 0
    ): ResponseComment

    @POST("/api/v1/posts/{id}/vote")
    suspend fun requestVote(
        @Path("id") id: Int,
        @Body body: RequestVote
    ): ResponseId

    @DELETE("/api/v1/posts/{id}")
    suspend fun requestVoteDelete(
        @Path("id") id: Int,
    ): ResponseId

    @POST("/api/v1/comment")
    suspend fun requestVotePostComment(
        @Body body: RequestPostComment
    ): ResponsePostComment
}
