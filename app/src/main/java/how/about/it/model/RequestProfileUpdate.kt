package how.about.it.model

import com.google.gson.annotations.SerializedName

data class RequestProfileUpdate (
    @SerializedName("name")
    var nickname : String?= null,
    @SerializedName("password")
    var password : String?= null
)