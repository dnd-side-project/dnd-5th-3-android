package com.moo.mool.network.emoji

import com.moo.mool.view.comment.model.RequestPostEmoji
import com.moo.mool.view.comment.model.RequestPutEmoji
import com.moo.mool.view.comment.model.ResponseEmoji
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface EmojiInterface {
    @POST("/api/v1/emoji")
    suspend fun requestPostEmoji(
        @Body body: RequestPostEmoji
    ): ResponseEmoji

    @PUT("/api/v1/emoji")
    suspend fun requestPutEmoji(
        @Body body: RequestPutEmoji
    ): ResponseEmoji
}
