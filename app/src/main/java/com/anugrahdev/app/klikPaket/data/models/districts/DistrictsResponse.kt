package com.anugrahdev.app.klikPaket.data.models.districts


import com.google.gson.annotations.SerializedName

data class DistrictsResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("statusCode")
    val statusCode: Int
)