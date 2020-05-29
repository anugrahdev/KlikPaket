package com.anugrahdev.app.klikPaket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.data.models.shippingcost.ShippingCostResult
import com.anugrahdev.app.klikPaket.databinding.ItemShippingcostBinding

class CostAdapter(private val cost:List<ShippingCostResult>): RecyclerView.Adapter<CostAdapter.CostViewHolder>() {

    inner class CostViewHolder(val item:ItemShippingcostBinding):RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CostViewHolder(
            DataBindingUtil.inflate<ItemShippingcostBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_shippingcost,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = cost.size

    override fun onBindViewHolder(holder: CostViewHolder, position: Int) {
        holder.item.shippingcost = cost[position]
        holder.item.root.setOnClickListener {
            if (cost[position].description != "")
                Toast.makeText(it.context, cost[position].description, Toast.LENGTH_SHORT).show()
        }
    }


}