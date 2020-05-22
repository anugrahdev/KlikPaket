package com.anugrahdev.app.ikurir.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    var today: String? = null
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

        iv_scan.setOnClickListener {
            initScanner()
        }

        val dateNow = Calendar.getInstance().time
        today = DateFormat.format("EEEE", dateNow) as String
        getToday()

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
        cv_setting.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_settingFragment, null))
        cv_myshipment.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_myShipmentFragment, null))

    }

    private fun getToday() {
        val date = Calendar.getInstance().time
        val tanggal = DateFormat.format("d MMMM yyyy", date) as String
        val formatFix: String = today.toString() + ", " + tanggal
        tvDate.setText(formatFix)
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