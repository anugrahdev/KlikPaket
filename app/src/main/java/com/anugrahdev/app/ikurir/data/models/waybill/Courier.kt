package com.anugrahdev.app.ikurir.data.models.waybill


import com.google.gson.annotations.SerializedName

data class Courier(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("service_code")
    val serviceCode: String
)