package com.anugrahdev.app.klikPaket.data.models.shippingcost


import com.google.gson.annotations.SerializedName

data class ShippingCostResult(
    @SerializedName("cost")
    val cost: String,
    @SerializedName("courier")
    val courier: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("estimate")
    val estimate: String,
    @SerializedName("service")
    val service: String
)