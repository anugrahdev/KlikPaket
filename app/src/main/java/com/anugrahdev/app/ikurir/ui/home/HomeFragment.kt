package com.anugrahdev.app.ikurir.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.anugrahdev.app.ikurir.R
import kotlinx.android.synthetic.main.home_fragment.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val date = Date()
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        val hour: Int = cal.get(Calendar.HOUR_OF_DAY)

        if (hour in 1..10){
            tv_greet.text = "Selamat Pagi"
        }else if(hour in 11..15){
            tv_greet.text = "Selamat Siang"
        }else if(hour in 15..16){
            tv_greet.text = "Selamat Sore"
        }else{
            tv_greet.text = "Selamat Malam"
        }

        cv_cost.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_costFragment, null))
        cv_track.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_trackFragment, null))

    }
}