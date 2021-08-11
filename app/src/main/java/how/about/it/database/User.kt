package how.about.it.database

import java.util.*

data class User(
    var access_token: String? = null,
    var refresh_token: String? = null,
    var user_id: String?= null,
    var email:String? = null,
    var password: String? = null,
    var nickname: String? = null,
    var profile_image_url: String?= null,
    var role: String?= null,
    var created_date: Date?= null,
    var updated_date: Date?= null,
)
