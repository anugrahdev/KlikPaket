package com.anugrahdev.app.klikPaket.data.models.districts


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)