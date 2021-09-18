package com.moo.mool.network.comment

import com.moo.mool.view.comment.model.RequestPostReComment
import com.moo.mool.view.comment.model.ResponseComment
import com.moo.mool.view.commentupdate.model.RequestPutComment
import com.moo.mool.view.vote.model.RequestCommentId
import com.moo.mool.view.vote.model.ResponsePostComment
import retrofit2.http.*

interface CommentInterface {
    @GET("/api/v1/comment/{id}")
    suspend fun requestGetReply(
        @Path("id") id: Int,
    ): ResponseComment

    @POST("/api/v1/comment/{commentId}/reply")
    suspend fun requestPostReply(
        @Path("commentId") commentId: Int,
        @Body body: RequestPostReComment
    ): ResponsePostComment

    @PUT("/api/v1/comment")
    suspend fun requestUpdateComment(
        @Body body: RequestPutComment,
    ): ResponsePostComment

    @HTTP(method = "DELETE", path = "/api/v1/comment", hasBody = true)
    suspend fun requestDeleteComment(
        @Body body: RequestCommentId
    ): ResponsePostComment
}
