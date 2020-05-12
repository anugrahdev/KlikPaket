package com.anugrahdev.app.ikurir.data.models.shippingcost


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("destination")
    val destination: Destination,
    @SerializedName("origin")
    val origin: Origin,
    @SerializedName("query")
    val query: Query,
    @SerializedName("results")
    val results: List<ShippingCostResult>
)