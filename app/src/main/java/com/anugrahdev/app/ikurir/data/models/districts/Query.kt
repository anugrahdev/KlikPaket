package com.anugrahdev.app.ikurir.data.models.districts


import com.google.gson.annotations.SerializedName

data class Query(
    @SerializedName("city")
    val city: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("q")
    val q: String
)