package com.anugrahdev.app.klikPaket.data.models.waybill


data class Courier(
    val code: String?=null,
    val name: String?=null
){
    constructor() : this(null,null)
}