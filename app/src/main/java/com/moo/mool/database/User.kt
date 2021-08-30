package com.moo.mool.database

import java.util.*

data class User(
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var userId: Long?= null,
    var email:String? = null,
    var password: String? = null,
    var nickname: String? = null,
    var role: String?= null,
    var createdDate: Date?= null,
    var updatedDate: Date?= null,
)
