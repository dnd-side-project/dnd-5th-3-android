package how.about.it.view.feed

import com.google.gson.annotations.SerializedName

data class ResponseMainFeed(
    @SerializedName("posts")
    val posts: List<Feed>
)

data class Feed(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("productImageUrl")
    val productImageUrl: String,
    @SerializedName("isVoted")
    val isVoted: Boolean,
    @SerializedName("permitRatio")
    val permitRatio: Int,
    @SerializedName("rejectRatio")
    val rejectRation: Int,
    @SerializedName("createdDate")
    val createdDate: String
)
