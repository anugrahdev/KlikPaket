package com.anugrahdev.app.klikPaket.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anugrahdev.app.klikPaket.ui.myshipment.DeliveredFragment
import com.anugrahdev.app.klikPaket.ui.myshipment.OnProcessFragment

class MyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return OnProcessFragment()
            1 -> return DeliveredFragment()
        }
        return OnProcessFragment()
    }


}