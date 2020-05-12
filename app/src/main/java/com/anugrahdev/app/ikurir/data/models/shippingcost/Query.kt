package com.anugrahdev.app.ikurir.data.models.shippingcost


import com.google.gson.annotations.SerializedName

data class Query(
    @SerializedName("courier")
    val courier: String,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("weight")
    val weight: String
)