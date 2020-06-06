package com.anugrahdev.app.klikPaket.ui.home

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
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.adapter.SliderAdapter
import com.anugrahdev.app.klikPaket.data.models.Slider
import com.anugrahdev.app.klikPaket.preferences.PreferenceProvider
import com.anugrahdev.app.klikPaket.utils.Utils
import com.anugrahdev.app.klikPaket.utils.snackbar
import com.google.zxing.integration.android.IntentIntegrator
import com.smarteist.autoimageslider.IndicatorAnimations
import kotlinx.android.synthetic.main.home_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.*

class HomeFragment : Fragment() , KodeinAware {
    override val kodein by kodein()
    private val prefs: PreferenceProvider by instance<PreferenceProvider>()
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

        tvDate.setText(Utils.DateFormat(LocalDateTime.now().toString(),prefs.getLanguage().toString()))
        val sliderList = listOf(
            Slider(R.drawable.ic_slidertwo), Slider(R.drawable.ic_sliderone), Slider(R.drawable.banner))
        val sliderAdapter by lazy{ SliderAdapter(sliderList) }
        imageSlider.setSliderAdapter(sliderAdapter)
        imageSlider.startAutoCycle()
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
        et_waybill.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
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