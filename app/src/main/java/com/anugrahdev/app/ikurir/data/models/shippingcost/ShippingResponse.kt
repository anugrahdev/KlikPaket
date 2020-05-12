package com.anugrahdev.app.ikurir.data.models.shippingcost


import com.google.gson.annotations.SerializedName

data class ShippingResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String,
    @SerializedName("statusCode")
    val statusCode: Int
)