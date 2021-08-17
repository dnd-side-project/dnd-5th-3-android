package how.about.it.model

import com.google.gson.annotations.SerializedName
import java.io.File

data class RequestUploadPost (
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("file")
    val file: File?
)

data class ResponseUploadPost (
    @SerializedName("id")
    val id: String
)