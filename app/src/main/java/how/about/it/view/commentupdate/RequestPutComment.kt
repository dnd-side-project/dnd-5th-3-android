package how.about.it.view.commentupdate

import com.google.gson.annotations.SerializedName

data class RequestPutComment(
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("content")
    val content: String
)
