package com.anugrahdev.app.klikPaket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.data.models.Slider
import com.anugrahdev.app.klikPaket.databinding.ItemSliderBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(
    private var mSliderItems: List<Slider>
) : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {


    inner class SliderAdapterVH(val item : ItemSliderBinding): ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return  SliderAdapterVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_slider,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        viewHolder.item.data= mSliderItems[position]
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }



    private var onItemClickListener: (() -> Unit)? = null

    fun setOnSliderClickListener(listener: () -> Unit) {
        onItemClickListener = listener
    }

}