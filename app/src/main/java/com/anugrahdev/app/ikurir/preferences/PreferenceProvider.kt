package com.anugrahdev.app.ikurir.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val KEY_LANGUAGE = "key_language"

class PreferenceProvider(context: Context) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun setLanguage(language:String){
        preference.edit().putString(
            KEY_LANGUAGE,
            language
        ).apply()
    }

    fun getLanguage():String?{
        return preference.getString(KEY_LANGUAGE, "Bahasa Indonesia")
    }



}