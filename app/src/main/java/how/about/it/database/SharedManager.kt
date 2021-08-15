package how.about.it.database

import android.content.Context
import android.content.SharedPreferences
import how.about.it.database.PreferenceHelper.get
import how.about.it.database.PreferenceHelper.set
class SharedManager(context: Context) {
    private val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context)

    fun saveCurrentUser(user: User) {
        prefs["accessToken"] = user.accessToken
        prefs["refreshToken"] = user.refreshToken
        prefs["userId"] = user.userId
        prefs["email"] = user.email
        prefs["nickname"] = user.nickname
    }
    fun getCurrentUser() : User {
        return User().apply {
            accessToken = prefs["accessToken", ""]
            refreshToken = prefs["refreshToken", ""]
            userId = prefs["userId", 0]
            email = prefs["email", ""]
            nickname = prefs["nickname", ""]
        }
    }

    fun saveAccessToken(accessToken : String){
        prefs["accessToken"] = accessToken
    }
    fun getRefreshToken() = prefs["refreshToken", ""]
}