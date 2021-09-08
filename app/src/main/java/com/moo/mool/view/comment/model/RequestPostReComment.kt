package com.moo.mool.view.comment.model

import com.google.gson.annotations.SerializedName

data class RequestPostReComment(
    @SerializedName("commentLayer")
    val commentLayer: Int = 1,
    @SerializedName("content")
    val content: String
)
