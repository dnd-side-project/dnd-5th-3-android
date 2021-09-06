package com.moo.mool.network.vote

import com.moo.mool.view.comment.model.RequestPutEmoji
import com.moo.mool.view.comment.model.ResponseComment
import com.moo.mool.view.comment.model.ResponseEmoji
import com.moo.mool.view.vote.model.*
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

    @HTTP(method = "DELETE", path = "/api/v1/comment", hasBody = true)
    suspend fun requestCommentDelete(
        @Body body: RequestCommentId
    ): ResponsePostComment

    @PUT("/api/v1/emoji")
    suspend fun requestPutEmoji(
        @Body body: RequestPutEmoji
    ): ResponseEmoji
}
