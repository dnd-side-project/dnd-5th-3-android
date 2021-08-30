package com.moo.mool.model

import com.google.gson.annotations.SerializedName

data class RequestProfileUpdate (
    @SerializedName("name")
    var nickname : String?= null,
    @SerializedName("password")
    var password : String?= null
)

data class RequestOldPasswordCheck(
    @SerializedName("password")
    var password : String?= null
)