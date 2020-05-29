package com.anugrahdev.app.klikPaket.data.models.shippingcost


import com.google.gson.annotations.SerializedName

data class Destination(
    @SerializedName("city")
    val city: String,
    @SerializedName("district_id")
    val districtId: String,
    @SerializedName("district_name")
    val districtName: String,
    @SerializedName("province")
    val province: String
)