package com.anugrahdev.app.klikPaket.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.preferences.PreferenceProvider
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var prefs: PreferenceProvider
    var lang:String="id"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferenceProvider(this)
        setContentView(R.layout.main_activity)
        setAppLocale(prefs.getLanguage()!!)


    }

    @Suppress("DEPRECATION")
    private fun setAppLocale(localeCode: String) {
        val resources: Resources = resources
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration
        config.setLocale(Locale(localeCode.toLowerCase(Locale.ROOT)))
        resources.updateConfiguration(config, dm)
    }

}
