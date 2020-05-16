package com.anugrahdev.app.ikurir.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.app.ikurir.R
import com.anugrahdev.app.ikurir.data.models.waybill.WaybillData
import com.anugrahdev.app.ikurir.databinding.ItemWaybilldetailBinding
import com.anugrahdev.app.ikurir.databinding.ItemWaybillhistoryBinding

class WaybillAdapter():RecyclerView.Adapter<WaybillAdapter.WaybillListViewHolder>() {

    inner class WaybillListViewHolder(val item:ItemWaybillhistoryBinding, viewType: Int):RecyclerView.ViewHolder(item.root)

    private val differCallback = object : DiffUtil.ItemCallback<WaybillData>(){
        override fun areItemsTheSame(oldItem: WaybillData, newItem: WaybillData): Boolean {
            return oldItem.waybillNumber == newItem.waybillNumber
        }

        override fun areContentsTheSame(oldItem: WaybillData, newItem: WaybillData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,  differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaybillListViewHolder {
        return WaybillListViewHolder(
            DataBindingUtil.inflate<ItemWaybillhistoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_waybillhistory,
            parent,
            false), viewType)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: WaybillListViewHolder, position: Int) {
        val waybill = differ.currentList[position]
        holder.itemView.apply {
            holder.item.waybill = waybill
            setOnClickListener {
                onItemClickListener?.let{ it(waybill) }
            }
        }
    }

    private var onItemClickListener: ((WaybillData)->Unit)? = null

    fun setOnItemClickListener(listener : (WaybillData)->Unit){
        onItemClickListener = listener
    }

}