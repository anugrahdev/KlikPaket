package com.anugrahdev.app.klikPaket.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*



@SuppressLint("SimpleDateFormat")
fun dateFormat(oldstringDate: String, locale: String): String? {
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

fun convertCountryCode(key: String): String{
    var lang=""
    when(key){
        "Bahasa Indonesia" -> lang="id"
        "English" -> lang="en"
        "en" -> lang="English"
        "id" -> lang="Bahasa Indonesia"
    }
    return lang
}

fun Context.copyToClipboard(text: CharSequence){
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label",text)
    clipboard.setPrimaryClip(clip)
}