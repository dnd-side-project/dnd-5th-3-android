package how.about.it.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseNotice (
    @SerializedName("noticeList")
    val noticeList: List<Notice>
)

@Entity
data class Notice(
    @SerializedName("noticeId")
    val noticeId: Int,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("content")
    var content: String? = null,
    @SerializedName("createdDate")
    var createdDate: String?= null,
    @SerializedName("updatedDate")
    var updatedDate: String?= null
)