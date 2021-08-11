package how.about.it.database

import android.content.Context
import android.content.SharedPreferences
import how.about.it.database.PreferenceHelper.get
import how.about.it.database.PreferenceHelper.set
class SharedManager(context: Context) {
    private val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context)

    fun saveCurrentUser(user: User) {
        prefs["access_token"] = user.access_token
        prefs["refresh_token"] = user.refresh_token
        prefs["user_id"] = user.user_id
        prefs["email"] = user.email
        prefs["nickname"] = user.nickname
        prefs["profile_image_url"] = user.profile_image_url
    }
    fun getCurrentUser() : User {
        return User().apply {
            access_token = prefs["access_token", ""]
            refresh_token = prefs["refresh_token", ""]
            user_id = prefs["user_id", ""]
            email = prefs["email", ""]
            nickname = prefs["nickname", ""]
            profile_image_url = prefs["profile_image_url"]
        }
    }
}