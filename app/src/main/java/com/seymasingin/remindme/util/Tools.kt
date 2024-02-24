package com.seymasingin.remindme.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

fun convertTimeToLong(date: LocalDate?, time: String): Long {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateString = dateFormat.format(date)
    val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    val dateTime = format.parse("$dateString $time") ?: return 0L
    return dateTime.time
}

fun convertStringToDate(string: String): LocalDate? {
    return LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
}

fun openLink(mContext: Context, url: String){
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(url)
    mContext.startActivity(openURL)
}