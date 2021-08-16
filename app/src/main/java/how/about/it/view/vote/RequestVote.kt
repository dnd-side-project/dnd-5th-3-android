package how.about.it.view.vote

import com.google.gson.annotations.SerializedName

data class RequestVote(
    @SerializedName("result")
    val result: String
)

data class ResponseId(
    @SerializedName("id")
    val id: Int
)
