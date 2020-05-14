package com.anugrahdev.app.ikurir.data.models.waybill


import com.google.gson.annotations.SerializedName

data class WaybillDetail(
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("shipping_code")
    val shippingCode: Int,
    @SerializedName("shipping_date")
    val shippingDate: String,
    @SerializedName("shipping_description")
    val shippingDescription: String,
    @SerializedName("shipping_time")
    val shippingTime: String
)