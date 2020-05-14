package com.anugrahdev.app.ikurir.data.models.waybill


import com.google.gson.annotations.SerializedName

data class DeliveryStatus(
    @SerializedName("pod_date")
    val podDate: String,
    @SerializedName("pod_name")
    val podName: String,
    @SerializedName("pod_time")
    val podTime: String,
    @SerializedName("status")
    val status: String
)