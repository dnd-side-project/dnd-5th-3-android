package com.moo.mool.view.comment

import com.google.gson.annotations.SerializedName

data class RequestPutEmoji(
    @SerializedName("commentEmojiId")
    val commentEmojiId: Int,
    @SerializedName("isChecked")
    val isChecked: Boolean
)
