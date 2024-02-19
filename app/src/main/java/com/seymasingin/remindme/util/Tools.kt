package com.seymasingin.remindme.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Tools {

    companion object {
        fun openLink(mContext: Context, url: String){
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(url)
            mContext.startActivity(openURL)
        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat( "dd MMM yyyy")
            return format.format(date)
        }

        /*fun convertTimeToLong(date: Date, time: String): Long {
            val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            val dateTime = format.parse("$date $time") ?: return 0L
            return dateTime.time
        }*/
    }
}