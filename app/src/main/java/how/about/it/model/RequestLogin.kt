package how.about.it.model

import com.google.gson.annotations.SerializedName

// 아이디와 비밀번호로 로그인 요청 방식
data class RequestLogin(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
)

data class ResponseLogin(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name : String,
    @SerializedName("accessToken")
    val accessToken : String,
    @SerializedName("refreshToken")
    val refreshToken : String
)

data class RequestTokenRefresh(
    @SerializedName("email")
    var email: String,
    @SerializedName("refreshToken")
    var refreshToken: String
)

data class ResponseTokenRefresh(
    @SerializedName("email")
    val email: String,
    @SerializedName("accessToken")
    val accessToken: String
)