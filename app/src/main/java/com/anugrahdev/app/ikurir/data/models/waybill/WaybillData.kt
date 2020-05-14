package com.anugrahdev.app.ikurir.data.models.waybill


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class WaybillData(
    @SerializedName("courier")
    val courier: Courier,
    @SerializedName("delivery_status")
    val deliveryStatus: DeliveryStatus,
    val details: List<WaybillDetail>,
    val waybill: Waybill


)

