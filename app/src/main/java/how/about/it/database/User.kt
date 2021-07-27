package how.about.it.database

import android.net.Uri

data class User(
    var accessToken : String? = null,
    var refreshToken : String? = null,
    var email:String? = null,
    var password :String? = null,
    var nickname:String? = null,
    var profilePhoto: Uri? = null
)
