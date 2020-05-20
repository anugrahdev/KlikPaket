package com.anugrahdev.app.ikurir.ui.trackwaybill

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
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.varunest.sparkbutton.SparkEventListener
import kotlinx.android.synthetic.main.trackwaybill_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDateTime


class TrackWaybillFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    val factory: WaybillViewModelFactory by instance<WaybillViewModelFactory>()
    lateinit var binding: TrackwaybillFragmentBinding
    val TAG="TrackWaybillFragment"
    var waybill:String?=null
    var courier:String?=null
    private val args:TrackWaybillFragmentArgs by navArgs()
    lateinit var historyitemAdapter:WaybillAdapter

    companion object {
        fun newInstance() = TrackWaybillFragment()
    }

    private lateinit var viewModel: WaybillViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.trackwaybill_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,factory).get(WaybillViewModel::class.java)
        val loadingstyle: Sprite = Wave()
        spin_kit_progress_bar.setIndeterminateDrawable(loadingstyle)
        setupHistoryRecyclerView()
        btn_track.setOnClickListener {
            if (et_waybill.text.isNotEmpty() && courier!="") {
                val selectedCourier = spinner_courier_waybill.selectedItem.toString().toLowerCase()
                courier = selectedCourier.substring(0, selectedCourier.indexOf(' '))
                if (courier == "j&t") {
                    courier = "jnt"
                }
                waybill = et_waybill.text.toString().toUpperCase()
                trackWaybill(waybill!!, courier!!)
            }
        }

        if (args.waybillNumber!=null && args.courier!=null) {
            waybill=args.waybillNumber
            courier=args.courier
            et_waybill.setText(waybill)
            trackWaybill(waybill!!, courier!!)
        }else if(args.waybillNumber!=null){
            et_waybill.setText(args.waybillNumber)
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
                        .setTitle("Lacak Resi Ini?")
                        .setIcon(R.drawable.ic_parcel)
                        .setMessage(wbData.waybillNumber)
                        .setPositiveButton("YA"){ dialog, which ->
                            startLoading()
                            layout_nestedscroll.fullScroll(View.FOCUS_UP)
                            layout_nestedscroll.smoothScrollTo(0,0)
                            et_waybill.setText(wbData.waybillNumber)
                            waybill = wbData.waybillNumber
                            trackWaybill(waybill!!, wbData.courier.code!!)
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
        viewModel.getHisotrywaybill().observe(viewLifecycleOwner, Observer {
            if  (it.isEmpty()){
                layout_history.visibility = View.GONE
            }
            recycler_view_history.apply {
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
                view?.rootView?.let { Snackbar.make(it, "Successfully Delete History", Snackbar.LENGTH_LONG).apply {
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

    private fun trackWaybill(wbNumber:String, courier: String){
        startLoading()
        viewModel.getWaybillData(wbNumber,courier)
    }

    private fun trackWaybillProcess(){

        viewModel.waybilldata.observe(viewLifecycleOwner, Observer {waybilldata->
            if (waybilldata == null){
                root_layout.snackbar("Resi Tidak Ditemukan")
                stopLoading()
            }else{
                waybilldata.waybillNumber = waybill!!
                waybilldata.savedTime = LocalDateTime.now().toString()
                viewModel.saveWaybill(waybilldata)
                binding.waybill = waybilldata
                binding.tvWaybill.text = waybill

                recycler_view_waybill.apply {
                    layoutManager = LinearLayoutManager(requireContext())

                    if(waybilldata.courier.code.equals("jnt")){
                        adapter = WaybillDetailAdapter(waybilldata.details.reversed())
                    }else{
                        adapter = WaybillDetailAdapter(waybilldata.details)
                    }

                }

                if (args.waybillNumber!=null && args.courier!=null){
                    waybilldata.type="saved"
                    waybilldata.waybillName = args.waybillName!!
                    viewModel.saveWaybill(waybilldata)
                }


                btn_savewaybill.setOnClickListener {view->
                    MaterialDialog(requireContext()).show {
                        title(R.string.saveawbdialogtitle)
                        message(R.string.saveawbdialogmessage)
                        input(allowEmpty = false) { dialog, text ->
                            val inputField: EditText = dialog.getInputField()
                            inputField.setBackgroundResource(R.drawable.edittext)
                            inputField.setBackgroundColor(Color.WHITE)
                            inputField.height = 50
                            waybilldata.type="saved"
                            waybilldata.waybillName=text.toString()
                        }
                        positiveButton {dialog->
                            viewModel.saveWaybill(waybilldata)
                            activity?.root_layout?.snackbar("Resi berhasil disimpan")
                        }
                        positiveButton(R.string.save)
                        negativeButton(R.string.cancel)
                        icon(R.drawable.ic_box)
                    }
                }

                if (waybilldata.type=="saved"){
                    btn_save.setChecked(true)
                }else{
                    btn_save.setChecked(false)
                }

                btn_save.setOnClickListener {
                    if(btn_save.isChecked){
                        MaterialDialog(requireContext()).show {
                            title(R.string.deleteawbdialogtitle)
                            message(R.string.saveawbdialogmessage)
                            positiveButton {
                                viewModel.deleteSavedWaybill(waybilldata)
                                activity?.btn_save?.playAnimation()
                                activity?.btn_save?.setChecked(false);
                            }
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
                                waybilldata.type="saved"
                                waybilldata.waybillName=text.toString()
                            }
                            positiveButton {dialog->
                                viewModel.saveWaybill(waybilldata)
                                activity?.btn_save?.playAnimation()
                                activity?.btn_save?.setChecked(true);
                            }
                            positiveButton(R.string.save)
                            negativeButton(R.string.cancel)
                            icon(R.drawable.ic_box)
                        }
                    }

                }

                stopLoading()
            }

        })

        expandBtn.setOnClickListener {
            if (expandableLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cv_trackwaybill, AutoTransition())
                expandBtn.text = "LESS"

                expandableLayout.visibility = View.VISIBLE

            } else {
                TransitionManager.beginDelayedTransition(cv_trackwaybill, AutoTransition())
                Handler().postDelayed({
                    expandableLayout.visibility = View.GONE
                    expandBtn.text = "DETAIL"

                }, 220)

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
