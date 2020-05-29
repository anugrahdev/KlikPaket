package com.anugrahdev.app.klikPaket.data.models.shippingcost


import com.google.gson.annotations.SerializedName

data class Origin(
    @SerializedName("city_id")
    val cityId: String,
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("province")
    val province: String
)