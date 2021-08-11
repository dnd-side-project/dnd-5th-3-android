package how.about.it.database

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigInteger
import java.util.*

data class Post(
    var user_id: BigInteger?= null,
    var post_id: BigInteger?= null,
    var title: String?= null,
    var product_name: String?= null,
    var content: String? = null,
    var product_image_url: Uri?= null,
    var is_voted: Boolean?= null,
    var permit_count: Int?= null,
    var reject_count: Int?= null,
    var view_count: Int?= null,
    var created_date: Date?= null,
    var updated_date: Date?= null,
    var is_deleted: Boolean?= null
)

@Parcelize
@Entity
data class TempPost (
    var title: String? = null,
    var product_name: String?= null,
    var content: String? = null,
    var product_image: String?= null, // String 형태로 형변환하여 이미지 저장
    var created_date: String?= null
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var tempPostPosition = 0
}