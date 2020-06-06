package com.anugrahdev.app.klikPaket.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.anugrahdev.app.klikPaket.R
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun loadImage(view : ImageView, url:String?){
    Glide.with(view)
        .load(url)
        .placeholder(R.drawable.placeholder_image)
        .into(view)
}