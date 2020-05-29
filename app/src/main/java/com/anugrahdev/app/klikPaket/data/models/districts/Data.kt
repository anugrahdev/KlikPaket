package com.anugrahdev.app.klikPaket.data.models.districts


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("city")
    val city: List<Any>,
    @SerializedName("query")
    val query: Query,
    @SerializedName("results")
    val results: List<DistrictsResult>
)