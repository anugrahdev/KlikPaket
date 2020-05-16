package com.anugrahdev.app.ikurir.data.models.waybill


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

const val waybill_number = " ";

@Entity(primaryKeys = ["waybillNumber","type"])
data class WaybillData constructor(
    @Embedded(prefix = "courier_")
    var courier: Courier,
    @Embedded(prefix = "status_")
    var delivery_status: DeliveryStatus,
    @Embedded(prefix = "waybill_")
    var waybill: Waybill,
    @Ignore var details: List<WaybillDetail>



){
    var waybillNumber: String = waybill_number
    var type:String = "history"
    var savedTime:String = ""

    constructor():this(
        Courier(null,null),
        DeliveryStatus(null,null,null,null),
        Waybill(null,null,null,null,null,null,null,null,null,null,null),
        listOf(WaybillDetail(null,null,null,null,null))
    )
}

