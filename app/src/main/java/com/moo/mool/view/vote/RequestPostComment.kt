package com.moo.mool.view.vote

data class RequestPostComment(
    val commentLayer: Int = 0,
    val postId: Int,
    val content: String
)