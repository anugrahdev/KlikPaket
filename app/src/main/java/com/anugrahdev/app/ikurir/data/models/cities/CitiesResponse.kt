package com.anugrahdev.app.ikurir.data.models.cities


import com.google.gson.annotations.SerializedName

data class CitiesResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("statusCode")
    val statusCode: Int
)