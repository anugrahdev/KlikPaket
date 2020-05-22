package com.anugrahdev.app.ikurir.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.anugrahdev.app.ikurir.R
import com.anugrahdev.app.ikurir.preferences.PreferenceProvider
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var prefs: PreferenceProvider
    var lang:String="id"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferenceProvider(this)
        setContentView(R.layout.main_activity)
        when(prefs.getLanguage()){
            "Bahasa Indonesia" -> lang="id"
            "English" -> lang="en"
        }
        setAppLocale(lang);


    }

    private fun setAppLocale(localeCode: String) {
        val resources: Resources = resources
        val dm: DisplayMetrics = resources.getDisplayMetrics()
        val config: Configuration = resources.getConfiguration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(Locale(localeCode.toLowerCase()))
        } else {
            config.locale = Locale(localeCode.toLowerCase())
        }
        resources.updateConfiguration(config, dm)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }
}
