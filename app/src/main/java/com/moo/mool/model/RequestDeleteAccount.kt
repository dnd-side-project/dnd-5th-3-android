package com.moo.mool.model

import com.google.gson.annotations.SerializedName

class RequestDeleteAccount (
    @SerializedName("email")
    val email: String
)