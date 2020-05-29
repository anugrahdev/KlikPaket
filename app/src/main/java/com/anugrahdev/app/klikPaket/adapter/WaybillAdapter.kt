package com.anugrahdev.app.klikPaket.adapter

import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.data.models.waybill.WaybillData
import com.anugrahdev.app.klikPaket.databinding.ItemWaybillhistoryBinding
import kotlinx.android.synthetic.main.item_waybillhistory.view.cv_savedawb
import kotlinx.android.synthetic.main.item_waybillhistory.view.expand
import kotlinx.android.synthetic.main.item_waybillhistory.view.expandableLayout
import kotlinx.android.synthetic.main.item_waybillhistory.view.ic_courier
import kotlinx.android.synthetic.main.item_waybillhistory.view.ic_expand

class WaybillAdapter :RecyclerView.Adapter<WaybillAdapter.WaybillListViewHolder>() {

    inner class WaybillListViewHolder(val item: ItemWaybillhistoryBinding):RecyclerView.ViewHolder(item.root)

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
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_waybillhistory,
            parent,
            false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: WaybillListViewHolder, position: Int) {
        val waybill = differ.currentList[position]
        holder.itemView.expand.setOnClickListener {
            if (holder.itemView.expandableLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(holder.itemView.cv_savedawb, AutoTransition())
                holder.itemView.expandableLayout.visibility = View.VISIBLE
                holder.itemView.ic_expand.setImageResource(R.drawable.ic_arrow_drop_up)
            } else {
                Handler().postDelayed({
//                    TransitionManager.beginDelayedTransition(holder.itemView.cv_savedawb, AutoTransition())
                    holder.itemView.expandableLayout.visibility = View.GONE
                    holder.itemView.ic_expand.setImageResource(R.drawable.ic_arrow_drop_down)
                },200)

            }
        }
        holder.itemView.apply {
            holder.item.waybill = waybill
            when(waybill.courier.code){
                "sicepat" -> ic_courier.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_sicepat))
                "lion" -> ic_courier.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_lion))
                "jnt" -> ic_courier.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_jnt))
                "tiki" -> ic_courier.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_tiki))
            }
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