package com.anugrahdev.app.klikPaket.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Utils {



    fun DateFormat(oldstringDate: String, locale: String): String? {
        val newDate: String?
        val dateFormat =
            SimpleDateFormat("E, d MMMM yyyy", Locale(locale))
        newDate = try {
            val date: Date? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(oldstringDate)
            dateFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
            oldstringDate
        }
        return newDate
    }

}