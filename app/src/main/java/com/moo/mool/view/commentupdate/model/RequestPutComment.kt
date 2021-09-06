package com.moo.mool.view.commentupdate.model

import com.google.gson.annotations.SerializedName

data class RequestPutComment(
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("content")
    val content: String
)
