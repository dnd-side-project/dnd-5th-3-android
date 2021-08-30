package com.moo.mool.database

import android.content.Context
import android.content.SharedPreferences
import com.moo.mool.database.PreferenceHelper.get
import com.moo.mool.database.PreferenceHelper.set
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

    fun updateAccessToken(accessToken : String){
        prefs["accessToken"] = accessToken
    }
    fun getRefreshToken() = prefs["refreshToken", ""]

    fun updateNickname(nickname: String){
        prefs["nickname"] = nickname
    }

    fun setSkipOnBoardingFragment() {
        prefs["skipOnBoarding"] = "true"
    }
    fun isSkipOnBoardingFragment() = prefs["skipOnBoarding", ""]
}