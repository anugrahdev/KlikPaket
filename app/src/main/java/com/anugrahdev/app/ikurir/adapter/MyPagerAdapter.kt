package com.anugrahdev.app.ikurir.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anugrahdev.app.ikurir.ui.myshipment.DeliveredFragment
import com.anugrahdev.app.ikurir.ui.myshipment.OnProcessFragment

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