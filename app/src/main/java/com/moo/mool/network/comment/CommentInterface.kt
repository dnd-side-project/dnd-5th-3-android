package com.moo.mool.network.comment

import com.moo.mool.view.comment.*
import com.moo.mool.view.commentupdate.RequestPutComment
import com.moo.mool.view.vote.RequestCommentId
import com.moo.mool.view.vote.ResponsePostComment
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

    @HTTP(method = "DELETE", path = "/api/v1/comment", hasBody = true)
    suspend fun requestCommentDelete(
        @Body body: RequestCommentId
    ): ResponsePostComment

    @POST("/api/v1/emoji")
    suspend fun requestPostEmoji(
        @Body body: RequestPostEmoji
    ): ResponseEmoji

    @PUT("/api/v1/emoji")
    suspend fun requestPutEmoji(
        @Body body: RequestPutEmoji
    ): ResponseEmoji
}