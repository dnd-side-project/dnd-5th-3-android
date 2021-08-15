package how.about.it.util

import android.content.Context
import how.about.it.R
import java.text.SimpleDateFormat
import java.util.*


object TimeChangerUtil {
    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
    private val currentTime =
        requireNotNull(format.parse(format.format(System.currentTimeMillis()))).time

    fun timeChange(context: Context, time: String): String {
        val longTime = requireNotNull(format.parse(time.replace("T", " "))).time

        val seconds = (System.currentTimeMillis() - longTime) / 1000

        when {
            seconds < 60 -> {
                return context.getString(R.string.comment_time_now)
            }
            seconds < 60 * 60 -> {
                return getString(context, seconds / 60, R.string.comment_time_minute_ago)
            }
            seconds < 60 * 60 * 24 -> {
                return getString(context, seconds / 3600, R.string.comment_time_hour_ago)
            }
            seconds < 60 * 60 * 24 * 30 -> {
                return getString(context, seconds / 86400, R.string.comment_time_day_ago)
            }
            else -> {
                return getString(context, seconds / 31104000, R.string.comment_time_year_ago)
            }
        }
    }

    private fun getString(context: Context, time: Long, id: Int) = String.format(
        context.getString(R.string.comment_time),
        time,
        context.getString(id)
    )

    fun getRemainTime(time: String): Long {
        val longTime = requireNotNull(format.parse(time.replace("T", " "))).time
        val remainTime = (longTime - currentTime) / 1000
        return when {
            remainTime < 0 -> {
                -1
            }
            remainTime < 3600 -> {
                1
            }
            else -> {
                remainTime / 3600
            }
        }
    }

    fun getDeadLine(time: String): Long {
        val longTime = requireNotNull(format.parse(time.replace("T", " "))).time
        return (longTime - currentTime)
    }

    fun getDeadLineString(time: Long): String {
        val seconds = (time / 1000) % 60
        val minutes = (time / 1000) / 60 % 60
        val hours = (time / 1000) / (60 * 60)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
