package com.anugrahdev.app.klikPaket.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.anugrahdev.app.klikPaket.R
import com.vansuita.materialabout.builder.AboutBuilder
import com.vansuita.materialabout.views.AboutView

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: AboutView = AboutBuilder.with(this)
            .setPhoto(R.drawable.img_an)
            .setCover(R.mipmap.profile_cover)
            .setName("anugrahdev")
            .setSubTitle("Mobile Developer")
            .setBrief("Mobile Developer & Android Enthusiast")
            .setAppIcon(R.drawable.img_parcel)
            .setAppName(R.string.app_name)
            .addLinkedInLink("anangnugraha")
            .addGitHubLink("anugrahdev")
            .addFacebookLink("anangnugraha8")
            .addEmailLink("anangnugraha8@gmail.com")
            .addWhatsappLink("Anang Nugraha","+6285840764050")
            .addInstagramLink("anangnugraha8")
            .addTwitterLink("anangnugraha8")
            .addWebsiteLink("https://anugrahdev.github.io/")
            .addFiveStarsAction()
            .addMoreFromMeAction(null)
            .addFeedbackAction("anangnugraha8@gmail.com")
            .setVersionNameAsAppSubTitle()
            .addShareAction(R.string.app_name)
            .setWrapScrollView(true)
            .setLinksAnimated(true)
            .setShowAsCard(false)
            .setLinksColumnsCount(4)
            .setActionsColumnsCount(2)
            .build()
        setContentView(view)
    }
}