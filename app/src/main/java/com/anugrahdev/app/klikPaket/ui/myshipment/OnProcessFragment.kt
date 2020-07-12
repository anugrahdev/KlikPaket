package com.anugrahdev.app.klikPaket.ui.myshipment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.adapter.MyShipmentAdapter
import com.anugrahdev.app.klikPaket.data.models.waybill.WaybillData
import com.anugrahdev.app.klikPaket.ui.trackwaybill.WaybillViewModel
import com.anugrahdev.app.klikPaket.utils.copyToClipboard
import com.anugrahdev.app.klikPaket.utils.snackbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_delivered.*
import kotlinx.android.synthetic.main.fragment_onprocess.*
import kotlinx.android.synthetic.main.fragment_onprocess.cv_notfound
import kotlinx.android.synthetic.main.fragment_trackwaybill.*
import kotlinx.android.synthetic.main.layout_waybill_bottom_sheet.*

@AndroidEntryPoint
class OnProcessFragment : Fragment() {

    private val viewModel: WaybillViewModel by viewModels()
    lateinit var historyitemAdapter: MyShipmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onprocess, container, false)


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        getOnProcessShipment()


    }

    private fun setupRecyclerView() {
        historyitemAdapter = MyShipmentAdapter()
        recycler_view_onprocess.apply {
            adapter = historyitemAdapter.also {
                it.setOnItemClickListener { wbData ->
                    setupBottomSheetDialog(wbData)
                }
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setupBottomSheetDialog(wbData: WaybillData) {
        val bottomSheet =
            layoutInflater.inflate(R.layout.layout_waybill_bottom_sheet, null)
        BottomSheetDialog(requireContext()).also { bottomSheetDialog ->
            bottomSheetDialog.setContentView(bottomSheet)
            bottomSheetDialog.dismissWithAnimation = true
            bottomSheetDialog.tv_bs_courier.text = wbData.courier.name
            bottomSheetDialog.tv_bs_airwaybill.text = wbData.waybillNumber
            bottomSheetDialog.ll_bs_track.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.track_this))
                    .setIcon(R.drawable.ic_parcel)
                    .setMessage("${wbData.courier.name} - ${wbData.waybillNumber}")
                    .setPositiveButton("YES") { _, _ ->
                        val bundle = Bundle().apply {
                            putString("waybillNumber", wbData.waybillNumber)
                            putString("courier", wbData.courier.code)
                            putString("waybillName", wbData.waybillName)
                        }
                        findNavController().navigate(
                            R.id.action_myShipmentFragment_to_trackWaybillFragment,
                            bundle
                        )
                    }
                    .setNegativeButton("CANCEL") { _, _ ->
                    }
                    .show()
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.ll_bs_copy.setOnClickListener {
                requireContext().copyToClipboard(wbData.waybillNumber)
                root_layout.snackbar(
                    getString(R.string.copy_airwaybill_response)
                )
            }
            bottomSheetDialog.ll_bs_remove.setOnClickListener {
                if (wbData.history) {
                    wbData.saved = false
                    viewModel.updateWaybill(wbData)

                } else if (!wbData.history) {
                    viewModel.deleteSavedWaybill(wbData)
                }
                view?.rootView?.let {
                    Snackbar.make(it, "Successfully remove waybill data", Snackbar.LENGTH_LONG)
                        .apply {
                            setAction("Undo") {
                                if (!wbData.history) {
                                    viewModel.saveWaybill(wbData)
                                } else {
                                    wbData.saved = true
                                    viewModel.updateWaybill(wbData)
                                }
                                cv_notfound.visibility = View.GONE

                            }.show()
                        }
                }
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.show()
        }
    }


    private fun getOnProcessShipment() {
        viewModel.getSavedWaybill("ON PROGRESS").observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                cv_notfound.visibility = View.VISIBLE
            }
            recycler_view_onprocess.apply {
                val sortedHistory = it.sortedByDescending { sortBy ->
                    sortBy.savedTime
                }
                historyitemAdapter.differ.submitList(sortedHistory)
            }
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val wbData = historyitemAdapter.differ.currentList[position]
                if (wbData.history) {
                    wbData.saved = false
                    viewModel.updateWaybill(wbData)

                } else if (!wbData.history) {
                    viewModel.deleteSavedWaybill(wbData)
                }
                view?.rootView?.let {
                    Snackbar.make(it, "Successfully remove waybill data", Snackbar.LENGTH_LONG)
                        .apply {
                            setAction("Undo") {
                                if (!wbData.history) {
                                    viewModel.saveWaybill(wbData)
                                } else {
                                    wbData.saved = true
                                    viewModel.updateWaybill(wbData)
                                }
                                cv_notfound.visibility = View.GONE
                            }.show()
                        }
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recycler_view_onprocess)
        }


    }


}
