package com.anugrahdev.app.klikPaket.preferences

import com.chibatching.kotpref.KotprefModel

object SettingsPref: KotprefModel() {

    var language by stringPref(default = "Bahasa Indonesia")

}