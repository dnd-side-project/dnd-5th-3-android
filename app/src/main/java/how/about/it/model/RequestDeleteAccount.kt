package how.about.it.model

import com.google.gson.annotations.SerializedName

class RequestDeleteAccount (
    @SerializedName("email")
    val email: String
)