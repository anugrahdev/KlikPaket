package com.anugrahdev.app.ikurir.data.models.waybill


import com.google.gson.annotations.SerializedName

data class Waybill(
    @SerializedName("destination")
    val destination: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("receiver_address")
    val receiverAddress: String,
    @SerializedName("receiver_city")
    val receiverCity: String,
    @SerializedName("receiver_name")
    val receiverName: String,
    @SerializedName("shipper_address")
    val shipperAddress: String,
    @SerializedName("shipper_name")
    val shipperName: String,
    @SerializedName("shipping_city")
    val shippingCity: String,
    @SerializedName("waybill_date")
    val waybillDate: String,
//    @SerializedName("waybill_number")
//    val waybillNumber: List<String>,
    @SerializedName("waybill_time")
    val waybillTime: String,
    @SerializedName("weight")
    val weight: Int
)