package com.anugrahdev.app.klikPaket.data.models.waybill


import com.google.gson.annotations.SerializedName

data class DeliveryStatus(
    @SerializedName("pod_date")
    val podDate: String?=null,
    @SerializedName("pod_name")
    val podName: String?=null,
    @SerializedName("pod_time")
    val podTime: String?=null,
    @SerializedName("status")
    val status: String?=null
){
    constructor() : this(null,null,null,null)
}