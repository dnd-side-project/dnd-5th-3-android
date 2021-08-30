package com.moo.mool.util

import android.content.Context
import com.moo.mool.R
import java.text.SimpleDateFormat
import java.util.*


object TimeChangerUtil {
    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

    fun timeChange(context: Context, time: String): String {
        val seconds = (System.currentTimeMillis() - getStringTime(time)) / 1000

        when {
            seconds < 60 -> {
                return context.getString(R.string.comment_time_now)
            }
            seconds < 60 * 60 -> {
                return getStringFormat(context, seconds / 60, R.string.comment_time_minute_ago)
            }
            seconds < 60 * 60 * 24 -> {
                return getStringFormat(context, seconds / 3600, R.string.comment_time_hour_ago)
            }
            seconds < 60 * 60 * 24 * 30 -> {
                return getStringFormat(context, seconds / 86400, R.string.comment_time_day_ago)
            }
            else -> {
                return getStringFormat(context, seconds / 31104000, R.string.comment_time_year_ago)
            }
        }
    }

    private fun getStringTime(time: String) =
        requireNotNull(format.parse(time.replace("T", " "))).time

    private fun getStringFormat(context: Context, time: Long, id: Int) = String.format(
        context.getString(R.string.comment_time),
        time,
        context.getString(id)
    )

    fun getRemainTime(time: String): Long {
        val currentTime =
            requireNotNull(format.parse(format.format(System.currentTimeMillis()))).time
        val remainTime = (getStringTime(time) - currentTime) / 1000
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
        val currentTime =
            requireNotNull(format.parse(format.format(System.currentTimeMillis()))).time
        return (getStringTime(time) - currentTime)
    }

    fun getDeadLineString(time: Long): String {
        val seconds = (time / 1000) % 60
        val minutes = (time / 1000) / 60 % 60
        val hours = (time / 1000) / (60 * 60)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
