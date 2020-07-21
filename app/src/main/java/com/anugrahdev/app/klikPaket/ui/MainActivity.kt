package com.anugrahdev.app.klikPaket.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.preferences.SettingsPref
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAppLocale(SettingsPref.language)


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
