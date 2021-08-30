package com.moo.mool.model

import com.google.gson.annotations.SerializedName

data class RequestResetPassword(
    @SerializedName("email")
    var email : String?= null
)