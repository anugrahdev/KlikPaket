package com.anugrahdev.app.ikurir.data.models.waybill


import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

data class Courier(
    val code: String?=null,
    val name: String?=null
){
    constructor() : this(null,null)
}