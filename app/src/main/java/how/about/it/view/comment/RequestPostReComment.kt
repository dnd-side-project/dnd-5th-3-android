package how.about.it.view.comment

import com.google.gson.annotations.SerializedName

data class RequestPostReComment(
    @SerializedName("commentLayer")
    val commentLayer: Int = 1,
    @SerializedName("content")
    val content: String
)
