package com.anugrahdev.app.klikPaket.ui.myshipment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.adapter.MyPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_myshipment.*

@AndroidEntryPoint
class MyShipmentFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_myshipment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view_pager.adapter = MyPagerAdapter(this)

        TabLayoutMediator(tab_layout, view_pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> { tab.text = "On Process"}
                1 -> { tab.text = "Delivered"}
            }
        }).attach()

        ic_back.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_myShipmentFragment_to_homeFragment, null))


    }


}
