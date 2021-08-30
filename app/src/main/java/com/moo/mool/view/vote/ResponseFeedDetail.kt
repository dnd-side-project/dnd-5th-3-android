package com.moo.mool.view.vote

import com.google.gson.annotations.SerializedName

data class ResponseFeedDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("productImageUrl")
    val productImageUrl: String,
    @SerializedName("isVoted")
    val isVoted: Boolean,
    @SerializedName("permitCount")
    val permitCount: Int,
    @SerializedName("rejectCount")
    val rejectCount: Int,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("voteDeadline")
    val voteDeadline: String,
    @SerializedName("currentMemberVoteResult")
    val currentMemberVoteResult: String
)
