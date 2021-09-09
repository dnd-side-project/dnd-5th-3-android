package com.moo.mool.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

data class Post(
    var userId: Long?= null,
    var postId: Long?= null,
    var title: String?= null,
    var content: String? = null,
    var productImageUrl: String?= null,
    var isVoted: Boolean?= null,
    var permitCount: Int?= null,
    var rejectCount: Int?= null,
    var viewCount: Int?= null,
    var createdDate: Date?= null,
    var updatedDate: Date?= null,
    var isDeleted: Boolean?= null
)

@Parcelize
@Entity
data class TempPost (
    var title: String? = null,
    var content: String? = null,
    var productImage: String?= null, // String 형태로 형변환하여 이미지 저장
    var productImageFileExtension: String?= null,
    var createdDate: String?= null
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var tempPostPosition = 0
}