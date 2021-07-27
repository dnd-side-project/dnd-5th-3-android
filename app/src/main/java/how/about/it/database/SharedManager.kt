package how.about.it.database

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import how.about.it.database.PreferenceHelper.set
import how.about.it.database.PreferenceHelper.get

class SharedManager(context: Context) {
    private val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context)

    fun saveCurrentUser(user: User) {
        prefs["accessToken"] = user.accessToken
        prefs["refreshToken"] = user.refreshToken
        prefs["email"] = user.email
        prefs["nickname"] = user.nickname
        prefs["profilePhoto"] = user.profilePhoto
    }

    fun getCurrentUser() : User {
        return User().apply {
            accessToken = prefs["accessToken", ""]
            refreshToken =  prefs["refreshToken", ""]
            email = prefs["email", ""]
            nickname = prefs["nickname", ""]
            profilePhoto = prefs["profilePhoto"]
        }
    }
}