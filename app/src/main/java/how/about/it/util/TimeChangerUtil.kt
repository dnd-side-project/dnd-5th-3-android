package how.about.it.util

import android.content.Context
import how.about.it.R
import java.text.SimpleDateFormat
import java.util.*


object TimeChangerUtil {
    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

    fun timeChange(context: Context, time: String): String {
        val longTime = requireNotNull(format.parse(time.replace("T", " "))).time

        val calTime = (System.currentTimeMillis() - longTime) / 1000

        when {
            calTime < 60 -> {
                return String.format(
                    context.getString(R.string.comment_time),
                    context.getString(R.string.comment_time_now)
                )
            }
            calTime < 60 * 60 -> {
                return getString(context, calTime / 60, R.string.comment_time_minute_ago)
            }
            calTime < 60 * 60 * 24 -> {
                return getString(context, calTime / 3600, R.string.comment_time_hour_ago)
            }
            calTime < 60 * 60 * 24 * 30 -> {
                return getString(context, calTime / 86400, R.string.comment_time_day_ago)
            }
            else -> {
                return getString(context, calTime / 31104000, R.string.comment_time_year_ago)
            }
        }
    }

    private fun getString(context: Context, time: Long, id: Int) = String.format(
        context.getString(R.string.comment_time),
        time,
        context.getString(id)
    )

    fun getDeadLine(time: String): Long {
        val longTime = requireNotNull(format.parse(time.replace("T", " "))).time
        val currentTime =
            requireNotNull(format.parse(format.format(System.currentTimeMillis()))).time
        return (longTime - currentTime)
    }

    fun getDeadLineString(time: Long): String {
        val seconds = (time / 1000) % 60
        val minutes = (time / 1000) / 60 % 60
        val hours = (time / 1000) / (60 * 60)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
