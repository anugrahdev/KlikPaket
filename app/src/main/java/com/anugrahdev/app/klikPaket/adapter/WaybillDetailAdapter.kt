package com.anugrahdev.app.klikPaket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.data.models.waybill.WaybillDetail
import com.anugrahdev.app.klikPaket.databinding.ItemWaybilldetailBinding
import com.github.vipulasri.timelineview.TimelineView
import kotlinx.android.synthetic.main.item_waybilldetail.view.*

class WaybillDetailAdapter(private val waybill:List<WaybillDetail>):RecyclerView.Adapter<WaybillDetailAdapter.WaybillViewHolder>() {

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