package com.anugrahdev.app.ikurir.ui.trackwaybill

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.app.ikurir.R
import com.anugrahdev.app.ikurir.adapter.WaybillAdapter
import com.anugrahdev.app.ikurir.adapter.WaybillDetailAdapter
import com.anugrahdev.app.ikurir.databinding.TrackwaybillFragmentBinding
import com.anugrahdev.app.ikurir.utils.hide
import com.anugrahdev.app.ikurir.utils.show
import com.anugrahdev.app.ikurir.utils.snackbar
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.trackwaybill_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDateTime


class TrackWaybillFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    val factory: TrackWaybillViewModelFactory by instance<TrackWaybillViewModelFactory>()
    lateinit var binding: TrackwaybillFragmentBinding
    val TAG="TrackWaybillFragment"
    var waybill:String?=null
    lateinit var historyitemAdapter:WaybillAdapter

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
        setupHistoryRecyclerView()
        trackWaybillProcess()
        getTrackHistory()
        btn_scan.setOnClickListener {
            initScanner()
        }



    }

    private fun setupHistoryRecyclerView(){
        historyitemAdapter = WaybillAdapter()
        recycler_view_history.apply {
            adapter = historyitemAdapter.also {
                it.setOnItemClickListener{ waybillData ->
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Lacak Resi Ini?")
                        .setIcon(R.drawable.ic_parcel)
                        .setMessage(waybillData.waybillNumber)
                        .setPositiveButton("YA"){ dialog, which ->
                            startLoading()
                            layout_nestedscroll.fullScroll(View.FOCUS_UP);
                            layout_nestedscroll.smoothScrollTo(0,0);
                            et_waybill.setText(waybillData.waybillNumber)
                            trackWaybill()
                        }
                        .setNegativeButton("CANCEL"){dialog, which ->
                        }
                        .show()
                }
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getTrackHistory(){
        viewModel.getSavedWaybill().observe(viewLifecycleOwner, Observer {
            recycler_view_history.apply {
//
                val sortedHistory = it.sortedByDescending { sortBy->
                    sortBy.savedTime
                }
                historyitemAdapter.differ.submitList(sortedHistory)
            }
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val waybill = historyitemAdapter.differ.currentList[position]
                viewModel.deleteSavedWaybill(waybill)
                view?.rootView?.let { Snackbar.make(it, "Successfully Delete Article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveWaybill(waybill)
                    }.show()
                } }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recycler_view_history)
        }


    }

    private fun trackWaybill(){
        startLoading()
        val selectedCourier = spinner_courier_waybill.selectedItem.toString().toLowerCase()
        var courier = selectedCourier.substring(0, selectedCourier.indexOf(' '));
        if (courier=="j&t") { courier="jnt" }
        waybill = et_waybill.text.toString().toUpperCase()
        if (et_waybill.text.isNotEmpty() && courier!=""){
            viewModel.getWaybillData(waybill!!,courier)
        }
    }

    private fun trackWaybillProcess(){

        btn_track.setOnClickListener {
            trackWaybill()
        }

        viewModel.waybilldata.observe(viewLifecycleOwner, Observer {waybilldata->
            if (waybilldata == null){
                root_layout.snackbar("Resi Tidak Ditemukan")
                stopLoading()
            }else{
                waybilldata.waybillNumber = waybill!!
                waybilldata.savedTime = LocalDateTime.now().toString()
                viewModel.saveWaybill(waybilldata)
                binding.waybill = waybilldata
                binding.tvWaybill.text = et_waybill.text.toString()

                recycler_view_waybill.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    val sortedShippingDetail = waybilldata.details.sortedBy {sort->
                        sort.shippingCode
                    }
                    adapter = WaybillDetailAdapter(sortedShippingDetail)
                    stopLoading()
                }

                btn_savewaybill.setOnClickListener {view->
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Simpan Resi Ini ?")
                        .setMessage("Resi ini akan tersimpan di dalam menu 'Daftar Kiriman'")
                        .setIcon(R.drawable.ic_box)
                        .setPositiveButton("YA"){dialog, which ->
                            waybilldata.type="tersimpan"
                            viewModel.saveWaybill(waybilldata)
                            root_layout.snackbar("Resi berhasil disimpan")
                        }
                        .setNegativeButton("CANCEL"){dialog, which ->  }
                        .show()
                }


                stopLoading()
            }

        })

        expandBtn.setOnClickListener {
            if (expandableLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cv_trackwaybill, AutoTransition())
                expandableLayout.visibility = View.VISIBLE
                expandBtn.text = "SHOW LESS"
            } else {
                TransitionManager.beginDelayedTransition(cv_trackwaybill, AutoTransition())
                expandableLayout.visibility = View.GONE
                expandBtn.text = "DETAIL"
            }
        }


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
                et_waybill.setText(result.contents.toString())
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun startLoading(){
        spin_kit_progress_bar.show()
    }

    private fun stopLoading(){
        spin_kit_progress_bar.hide()
    }

}
