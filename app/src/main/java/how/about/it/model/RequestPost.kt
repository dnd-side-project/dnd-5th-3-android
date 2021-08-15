package how.about.it.model

import com.google.gson.annotations.SerializedName

data class RequestUploadPost (
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("productImageUrl")
    val productImageUrl: String?
)

data class ResponseUploadPost (
    @SerializedName("id")
    val id: String
)