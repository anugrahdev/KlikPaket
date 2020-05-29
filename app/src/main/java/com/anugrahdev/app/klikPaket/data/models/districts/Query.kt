package com.anugrahdev.app.klikPaket.data.models.districts


import com.google.gson.annotations.SerializedName

data class Query(
    @SerializedName("city")
    val city: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("q")
    val q: String
)