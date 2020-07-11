package com.anugrahdev.app.klikPaket.ui.trackwaybill

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.adapter.WaybillAdapter
import com.anugrahdev.app.klikPaket.adapter.WaybillDetailAdapter
import com.anugrahdev.app.klikPaket.data.network.Resource
import com.anugrahdev.app.klikPaket.databinding.TrackwaybillFragmentBinding
import com.anugrahdev.app.klikPaket.utils.hide
import com.anugrahdev.app.klikPaket.utils.show
import com.anugrahdev.app.klikPaket.utils.snackbar
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.trackwaybill_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDateTime
import java.util.*

@AndroidEntryPoint
class TrackWaybillFragment : Fragment() {

    private lateinit var binding: TrackwaybillFragmentBinding
    private val viewModel: WaybillViewModel by viewModels()
    var waybill:String?=null
    var courier:String?=null
    private val args:TrackWaybillFragmentArgs by navArgs()
    lateinit var historyitemAdapter:WaybillAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.trackwaybill_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val loadingStyle: Sprite = Wave()
        spin_kit_progress_bar.setIndeterminateDrawable(loadingStyle)
        setupHistoryRecyclerView()
        btn_track.setOnClickListener {
            if (et_waybill.text.isNotEmpty() && courier!="") {
                val selectedCourier = spinner_courier_waybill.selectedItem.toString()
                    .toLowerCase(Locale.ROOT)
                courier = selectedCourier.substring(0, selectedCourier.indexOf(' '))
                if (courier == "j&t") {
                    courier = "jnt"
                }
                waybill = et_waybill.text.toString().toUpperCase(Locale.ROOT)
                trackWaybill(waybill!!, courier!!)
            }
        }

        if (args.waybillNumber!=null && args.courier!=null) {
            waybill=args.waybillNumber
            courier=args.courier
            et_waybill.setText(waybill)
            courierSelector(waybill!!)
            trackWaybill(waybill!!, courier!!)
        }else if(args.waybillNumber!=null){
            et_waybill.setText(args.waybillNumber)
            courierSelector(args.waybillNumber!!)
        }

        trackWaybillProcess()
        getTrackHistory()
        btn_scan.setOnClickListener {
            initScanner()
        }


        ic_back.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_trackWaybillFragment_to_homeFragment, null))



    }

    private fun setupHistoryRecyclerView(){
        historyitemAdapter = WaybillAdapter()
        recycler_view_history.apply {
            adapter = historyitemAdapter.also {
                it.setOnItemClickListener{ wbData ->
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.track_this))
                        .setIcon(R.drawable.ic_parcel)
                        .setMessage(wbData.waybillNumber)
                        .setPositiveButton("YES"){ _, _ ->
                            startLoading()
                            layout_nestedscroll.fullScroll(View.FOCUS_UP)
                            layout_nestedscroll.smoothScrollTo(0,0)
                            waybill = wbData.waybillNumber
                            et_waybill.setText(waybill)
                            courierSelector(waybill!!)
                            trackWaybill(waybill!!, wbData.courier.code!!)
                        }
                        .setNegativeButton("CANCEL"){ _, _ ->
                        }
                        .show()
                }
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getTrackHistory(){
        viewModel.getHisotrywaybill().observe(viewLifecycleOwner, Observer {
            if  (it.isEmpty()){
                layout_history.visibility = View.GONE
                cv_historyisempty.visibility = View.VISIBLE
            }else{
                cv_historyisempty.visibility = View.GONE
            }
            recycler_view_history.apply {
                val sortedHistory = it.sortedByDescending { sortBy->
                    sortBy.trackTime
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
                if(waybill.history && !waybill.saved){
                    viewModel.deleteSavedWaybill(waybill)
                }else{
                    waybill.history = false
                    viewModel.updateWaybill(waybill)
                }
                view?.rootView?.let { Snackbar.make(it, getString(R.string.delete_history_success), Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        if (waybill.saved){
                            waybill.history = true
                            viewModel.updateWaybill(waybill)
                        }else{
                            viewModel.saveWaybill(waybill)
                        }
                    }.show()
                } }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recycler_view_history)
        }


    }

    private fun trackWaybill(wbNumber:String, courier: String){
        startLoading()
        viewModel.getWaybillData(wbNumber,courier)
    }

    private fun trackWaybillProcess(){

        viewModel.waybillData.observe(viewLifecycleOwner, Observer {response->

            when (response) {
                is Resource.Success -> {
                    response.data?.let { data ->
                        binding.waybill = data
                        binding.tvWaybill.text = waybill
                        data.waybillNumber = waybill!!
                        CoroutineScope(Dispatchers.IO).launch {
                            if (viewModel.getSearchWaybill(data.waybillNumber)){
                                withContext(Dispatchers.Main){
                                    btn_save.isChecked = true
                                }
                                viewModel.updateSaveData(LocalDateTime.now().toString(), data.delivery_status.status.toString(), true, waybill!!)
                            }else{
                                withContext(Dispatchers.Main){
                                    btn_save.isChecked = false
                                }
                                data.trackTime = LocalDateTime.now().toString()
                                viewModel.saveWaybill(data)

                            }
                        }


                        recycler_view_waybill.apply {
                            layoutManager = LinearLayoutManager(requireContext())

                            adapter = if(data.courier.code.equals("jnt")){
                                WaybillDetailAdapter(data.details.reversed())
                            }else{
                                WaybillDetailAdapter(data.details)
                            }

                        }

                        if (args.waybillNumber!=null && args.courier!=null){
                            data.saved=true
                            data.trackTime = LocalDateTime.now().toString()
                            data.savedTime = LocalDateTime.now().toString()
                            data.waybillName = args.waybillName!!
                            viewModel.updateWaybill(data)
                        }

                        btn_save.setOnClickListener {
                            if(btn_save.isChecked){
                                MaterialDialog(requireContext()).show {
                                    title(R.string.deleteawbdialogtitle)
                                    message(R.string.deleteawbdialogmessage)
                                    positiveButton {
                                        if (data.history){
                                            data.saved = false
                                            viewModel.updateWaybill(data)
                                        }else if(!data.history && !data.saved){
                                            viewModel.deleteSavedWaybill(data)
                                        }
                                        activity?.btn_save?.playAnimation()
                                        activity?.btn_save?.isChecked = false
                                    }
                                    negativeButton(R.string.cancel)

                                }
                            }else{
                                MaterialDialog(requireContext()).show {
                                    title(R.string.saveawbdialogtitle)
                                    message(R.string.saveawbdialogmessage)
                                    input(allowEmpty = false) { dialog, text ->
                                        val inputField: EditText = dialog.getInputField()
                                        inputField.setBackgroundResource(R.drawable.edittext)
                                        inputField.setBackgroundColor(Color.WHITE)
                                        inputField.height = 50

                                        data.waybillName=text.toString()
                                    }
                                    positiveButton {
                                        data.saved=true
                                        data.savedTime = LocalDateTime.now().toString()
                                        viewModel.updateWaybill(data)
                                        activity?.btn_save?.playAnimation()
                                        activity?.btn_save?.isChecked = true
                                    }
                                    positiveButton(R.string.save)
                                    negativeButton(R.string.cancel)
                                    icon(R.drawable.ic_box)
                                }
                            }

                        }
                    }
                    stopLoading()
                }
                is Resource.Loading -> {
                    startLoading()
                }
                is Resource.Error -> {
                    response.message?.let { error ->
                        root_layout.snackbar(error)
                    }
                    stopLoading()
                }
            }

        })

        expandBtn.setOnClickListener {
            if (expandableLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cv_trackwaybill, AutoTransition())
                expandBtn.text = getString(R.string.collapsetext)
                expandableLayout.visibility = View.VISIBLE

            } else {
                TransitionManager.beginDelayedTransition(cv_trackwaybill, AutoTransition())
                Handler().postDelayed({
                    expandableLayout.visibility = View.GONE
                    expandBtn.text = getString(R.string.expandtext)
                }, 220)

            }
        }


    }

    private fun courierSelector(Waybill: String){
        if(Waybill.length == 16){
            spinner_courier_waybill.setSelection(0)
        }else if(Waybill.take(3) == "000"){
            spinner_courier_waybill.setSelection(2)
        }else if(Waybill.take(1) == "J" && Waybill.length<16){
            spinner_courier_waybill.setSelection(1)
        }else if(Waybill.length == 12){
            spinner_courier_waybill.setSelection(3)
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
                courierSelector(result.contents.toString())
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
