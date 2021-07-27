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
    val data : SomeData?
)

data class SomeData(
    val accessToken : String,
    val refreshToken : String
)