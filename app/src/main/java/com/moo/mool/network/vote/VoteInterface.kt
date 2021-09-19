package com.moo.mool.network.vote

import com.moo.mool.view.comment.model.ResponseComment
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

    @DELETE("/api/v1/posts/{id}")
    suspend fun requestVoteFeedDelete(
        @Path("id") id: Int,
    ): ResponseId

    @POST("/api/v1/posts/{id}/vote")
    suspend fun requestVote(
        @Path("id") id: Int,
        @Body body: RequestVote
    ): ResponseId

    @POST("/api/v1/comment")
    suspend fun requestVotePostComment(
        @Body body: RequestPostComment
    ): ResponsePostComment
}
