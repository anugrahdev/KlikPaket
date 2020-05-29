package com.anugrahdev.app.klikPaket.data.models.waybill


import com.google.gson.annotations.SerializedName


data class WaybillDetail(
    @SerializedName("city_name")
    val cityName: String?=null,
    @SerializedName("shipping_code")
    val shippingCode: Int?=null,
    @SerializedName("shipping_date")
    val shippingDate: String?=null,
    @SerializedName("shipping_description")
    val shippingDescription: String?=null,
    @SerializedName("shipping_time")
    val shippingTime: String?=null
)