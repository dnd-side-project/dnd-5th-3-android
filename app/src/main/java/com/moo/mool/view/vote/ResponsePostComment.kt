package com.moo.mool.view.vote

import com.google.gson.annotations.SerializedName

data class ResponsePostComment(
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("commentLayer")
    val commentLayer: Int = 0,
    @SerializedName("content")
    val content: String,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("updatedDate")
    val updatedDate: String
)