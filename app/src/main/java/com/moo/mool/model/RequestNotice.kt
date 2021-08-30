package com.moo.mool.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class ResponseNotice (
    @SerializedName("noticeList")
    val noticeList: List<Notice>
)

@Entity
data class Notice(
    @SerializedName("noticeId")
    val noticeId: Int,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("content")
    var content: String? = null,
    @SerializedName("createdDate")
    var createdDate: String?= null,
    @SerializedName("updatedDate")
    var updatedDate: String?= null
)