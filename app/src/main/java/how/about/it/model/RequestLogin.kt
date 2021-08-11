package how.about.it.model

// 아이디와 비밀번호로 로그인 요청 방식
data class RequestLogin(
    var userId: String,
    var password: String
)

data class ResponseLogin(
    val code: Int,
    val success : Boolean,
    val msg : String,
    val token : tokenData?,
    val nickname : String,
    val profile_image_url: String
)

data class tokenData(
    val access_token : String,
    val refresh_token : String
)