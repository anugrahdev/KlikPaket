package com.anugrahdev.app.klikPaket.data.models.districts


import com.google.gson.annotations.SerializedName

data class DistrictsResult(
    @SerializedName("city")
    val city: City,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)