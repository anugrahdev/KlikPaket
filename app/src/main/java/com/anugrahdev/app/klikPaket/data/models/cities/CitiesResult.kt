package com.anugrahdev.app.klikPaket.data.models.cities


import com.google.gson.annotations.SerializedName

data class CitiesResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("postal_code")
    val postalCode: String,
    @SerializedName("province_id")
    val provinceId: Int,
    @SerializedName("type")
    val type: String
)