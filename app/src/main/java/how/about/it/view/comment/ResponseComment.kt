package how.about.it.view.comment

import com.google.gson.annotations.SerializedName

data class ResponseComment(
    @SerializedName("commentList")
    val commentList: List<Comment>
)

data class Comment(
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("writerName")
    val writerName: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("voteType")
    val voteType: String,
    @SerializedName("replyCount")
    val replyCount: Int,
    @SerializedName("createDate")
    val createdDate: String,
    @SerializedName("emojiList")
    val emojiList: List<Emoji>
)

data class Emoji(
    @SerializedName("emojiId")
    val emojiId: Int,
    @SerializedName("emojiCount")
    var emojiCount: Int,
    @SerializedName("checked")
    var checked: Boolean
)