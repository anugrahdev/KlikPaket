package com.anugrahdev.app.ikurir.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

import com.anugrahdev.app.ikurir.R
import com.anugrahdev.app.ikurir.utils.snackbar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.home_fragment.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

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

        iv_scan.setOnClickListener {
            initScanner()
        }

        et_waybill.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                gotoTrack(et_waybill.text.toString())
                return@OnKeyListener true
            }
            false
        })

        cv_cost.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_costFragment, null))
        cv_track.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_trackWaybillFragment, null))
//        cv_nearby.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_nearbyFragment, null))
        cv_myshipment.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_myShipmentFragment, null))

    }

    private fun initScanner(){
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result!=null){
            if (result.contents==null){
                root_layout.snackbar("Cancelled")
            }else{
                gotoTrack(result.contents.toString())
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun gotoTrack(wbNumber:String){
        val bundle = Bundle().apply {
            putString("waybillNumber", wbNumber)
        }
        findNavController().navigate(R.id.action_homeFragment_to_trackWaybillFragment,bundle)
    }
}