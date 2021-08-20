package how.about.it.view.comment

import com.google.gson.annotations.SerializedName

data class ResponseEmoji(
    @SerializedName("commentEmojiId")
    val commentEmojiId: Int,
    @SerializedName("commentEmojiCount")
    val commentEmojiCount: Int
)
