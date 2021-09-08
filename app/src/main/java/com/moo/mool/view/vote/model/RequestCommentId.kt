package com.moo.mool.view.vote.model

import com.google.gson.annotations.SerializedName

data class RequestCommentId(
    @SerializedName("commentId")
    val commentId: Int
)