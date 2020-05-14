package com.anugrahdev.app.ikurir.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.app.ikurir.R
import com.anugrahdev.app.ikurir.data.models.waybill.WaybillDetail
import com.anugrahdev.app.ikurir.databinding.ItemWaybilldetailBinding
import com.github.vipulasri.timelineview.TimelineView
import kotlinx.android.synthetic.main.item_waybilldetail.view.*
import kotlinx.android.synthetic.main.trackwaybill_fragment.view.*

class WaybillAdapter(private val waybill:List<WaybillDetail>):RecyclerView.Adapter<WaybillAdapter.WaybillViewHolder>() {

    var mTimelineView: TimelineView? = null

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    inner class WaybillViewHolder(val item:ItemWaybilldetailBinding, viewType: Int):RecyclerView.ViewHolder(item.root){
        val timeline = itemView.timeline

        init {
            timeline.initLine(viewType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaybillViewHolder {

        return WaybillViewHolder(DataBindingUtil.inflate<ItemWaybilldetailBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_waybilldetail,
            parent,
            false), viewType)

    }




    override fun getItemCount() = waybill.size

    override fun onBindViewHolder(holder: WaybillViewHolder, position: Int) {
        holder.item.waybildetail = waybill[position]
    }


}