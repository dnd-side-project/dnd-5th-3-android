package com.moo.mool.view.comment.model

import com.google.gson.annotations.SerializedName

data class RequestPostEmoji(
    @SerializedName("emojiId")
    val emojiId: Int,
    @SerializedName("commentId")
    val commentId: Int
)
