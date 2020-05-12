package com.anugrahdev.app.ikurir.data.models.cities


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("province")
    val province: List<Any>,
    @SerializedName("query")
    val query: Query,
    @SerializedName("results")
    val results: List<CitiesResult>
)