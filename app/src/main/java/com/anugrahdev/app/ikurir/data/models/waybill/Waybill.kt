package com.anugrahdev.app.ikurir.data.models.waybill


import com.google.gson.annotations.SerializedName

data class Waybill(
    @SerializedName("destination")
    val destination: String? = null,
    @SerializedName("origin")
    val origin: String? = null,
    @SerializedName("receiver_address")
    val receiverAddress: String? =null,
    @SerializedName("receiver_city")
    val receiverCity: String? = null,
    @SerializedName("receiver_name")
    val receiverName: String? = null,
    @SerializedName("shipper_address")
    val shipperAddress: String? =null,
    @SerializedName("shipper_name")
    val shipperName: String? =null,
    @SerializedName("shipping_city")
    val shippingCity: String? =null,
    @SerializedName("waybill_date")
    val waybillDate: String? =null,
//    @SerializedName("waybill_number")
//    val waybillNumber: List<String>,
    @SerializedName("waybill_time")
    val waybillTime: String? =null,
    @SerializedName("weight")
    val weight: String? =null
){
    constructor() : this(null,null,null,null,null,null,null,null,null,null,null)
}