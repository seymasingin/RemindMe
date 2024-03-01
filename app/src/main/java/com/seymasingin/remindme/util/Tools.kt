package com.seymasingin.remindme.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Tools {

    companion object {

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return format.format(date)
        }
    }
}