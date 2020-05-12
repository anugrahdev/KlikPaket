package com.anugrahdev.app.ikurir.data.models.cities


import com.google.gson.annotations.SerializedName

data class Query(
    @SerializedName("id")
    val id: String,
    @SerializedName("province")
    val province: String,
    @SerializedName("q")
    val q: String
)