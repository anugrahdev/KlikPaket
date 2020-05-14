package com.anugrahdev.app.ikurir.ui.trackwaybill

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.app.ikurir.R
import com.anugrahdev.app.ikurir.adapter.WaybillAdapter
import com.anugrahdev.app.ikurir.databinding.TrackwaybillFragmentBinding
import com.anugrahdev.app.ikurir.utils.hide
import com.anugrahdev.app.ikurir.utils.show
import com.anugrahdev.app.ikurir.utils.snackbar
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import kotlinx.android.synthetic.main.trackwaybill_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class TrackWaybillFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    val factory: TrackWaybillViewModelFactory by instance<TrackWaybillViewModelFactory>()
    lateinit var binding: TrackwaybillFragmentBinding
    val TAG="TrackWaybillFragment"
    companion object {
        fun newInstance() = TrackWaybillFragment()
    }

    private lateinit var viewModel: TrackWaybillViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.trackwaybill_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,factory).get(TrackWaybillViewModel::class.java)
        val loadingstyle: Sprite = Wave()
        spin_kit_progress_bar.setIndeterminateDrawable(loadingstyle)

        btn_track.setOnClickListener {
            startLoading()
            val courier = spinner_courier_waybill.selectedItem.toString().toLowerCase()
            val resi = et_waybill.text.toString().toUpperCase()
            if (et_waybill.text.isNotEmpty() && courier!=""){
                viewModel.getWaybillData(resi,courier)
            }
        }

        expandBtn.setOnClickListener {
            if (expandableLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                expandableLayout.visibility = View.VISIBLE
                expandBtn.text = "SHOW LESS"
            } else {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                expandableLayout.visibility = View.GONE
                expandBtn.text = "DETAIL"
            }
        }

        viewModel.waybilldata.observe(viewLifecycleOwner, Observer {
            if (it == null){
                root_layout.snackbar("Resi Tidak Ditemukan")
                stopLoading()
            }else{
                binding.waybill = it
                binding.tvWaybill.text = et_waybill.text.toString()
                if(it.deliveryStatus.status=="DELIVERED"){
                    tv_delivery_status.setBackgroundResource(R.drawable.bg_green_rounded)
                }

                recycler_view_waybill.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = WaybillAdapter(it.details)
                    stopLoading()
                }
                stopLoading()
            }

        })


    }

    private fun startLoading(){
        spin_kit_progress_bar.show()
    }

    private fun stopLoading(){
        spin_kit_progress_bar.hide()
    }

}
