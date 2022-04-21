package com.udacity.asteroidradar.utils

import android.annotation.SuppressLint
import com.udacity.asteroidradar.Constants
import java.text.SimpleDateFormat
import java.util.*

object DayUtils {

    @SuppressLint("NewApi")
    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getStartDate(): String {
        val calendar = Calendar.getInstance()
        return formatDate(calendar.time)
    }

    fun getEndDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        return formatDate(calendar.time)
    }
}