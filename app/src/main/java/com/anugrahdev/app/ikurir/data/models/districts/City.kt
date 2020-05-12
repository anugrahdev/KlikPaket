package com.anugrahdev.app.ikurir.data.models.districts


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)