package how.about.it.view.comment

import com.google.gson.annotations.SerializedName

data class RequestPostEmoji(
    @SerializedName("emojiId")
    val emojiId: Int,
    @SerializedName("commentId")
    val commentId: Int
)
