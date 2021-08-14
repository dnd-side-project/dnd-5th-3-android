package how.about.it.model

import com.google.gson.annotations.SerializedName

data class RequestMember(
    @SerializedName("email")
    var email : String,
    @SerializedName("password")
    var password : String,
    @SerializedName("nickname")
    var nickname : String
)

data class ResponseMember(
    @SerializedName("memberId")
    var memberId : String,
    @SerializedName("email")
    var email : String,
    @SerializedName("password")
    var password : String,
    @SerializedName("nickname")
    var nickname : String
)