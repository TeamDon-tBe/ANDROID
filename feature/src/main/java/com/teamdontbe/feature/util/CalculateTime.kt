package com.teamdontbe.feature.util

import android.content.Context
import com.teamdontbe.feature.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CalculateTime(private val context: Context) {
    fun getCalculateTime(dateTimeString: String): String {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val targetDate = LocalDateTime.parse(dateTimeString, dateFormat)
        val currentDate = LocalDateTime.now()

        val minutesDifference = ChronoUnit.MINUTES.between(targetDate, currentDate)
        val hoursDifference = ChronoUnit.HOURS.between(targetDate, currentDate)
        val daysDifference = ChronoUnit.DAYS.between(targetDate, currentDate)
        val weeksDifference = ChronoUnit.WEEKS.between(targetDate, currentDate)
        val monthsDifference = ChronoUnit.MONTHS.between(targetDate, currentDate)
        val yearsDifference = ChronoUnit.YEARS.between(targetDate, currentDate)

        return when {
            minutesDifference < 1 -> context.getString(R.string.feed_time_now)
            hoursDifference < 1 -> "$minutesDifference${context.getString(R.string.feed_time_minute)}"
            daysDifference < 1 -> "$hoursDifference${context.getString(R.string.feed_time_hour)}"
            weeksDifference < 1 -> "$daysDifference${context.getString(R.string.feed_time_day)}"
            monthsDifference < 1 -> "$weeksDifference${context.getString(R.string.feed_time_week)}"
            yearsDifference < 1 -> "$monthsDifference${context.getString(R.string.feed_time_month)}"
            else -> "$yearsDifference${context.getString(R.string.feed_time_year)}"
        }
    }
}
