package how.about.it.database

import android.net.Uri
import java.math.BigInteger
import java.util.*

data class User(
    var access_token: String? = null,
    var refresh_token: String? = null,
    var user_id: BigInteger?= null,
    var email:String? = null,
    var password: String? = null,
    var nickname: String? = null,
    var profile_image_url: Uri?= null,
    var role: String?= null,
    var created_date: Date?= null,
    var updated_date: Date?= null,
)
