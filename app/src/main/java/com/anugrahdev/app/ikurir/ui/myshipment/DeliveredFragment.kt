package com.anugrahdev.app.ikurir.ui.myshipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anugrahdev.app.ikurir.R
import com.anugrahdev.app.ikurir.adapter.MyShipmentAdapter
import com.anugrahdev.app.ikurir.adapter.WaybillAdapter
import com.anugrahdev.app.ikurir.ui.trackwaybill.WaybillViewModel
import com.anugrahdev.app.ikurir.ui.trackwaybill.WaybillViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.delivered_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DeliveredFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    val factory: WaybillViewModelFactory by instance<WaybillViewModelFactory>()
    private lateinit var viewModel: WaybillViewModel
    lateinit var historyitemAdapter: MyShipmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.delivered_fragment, container, false)


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(WaybillViewModel::class.java)
        setupRecyclerView()
        getDeliveredShipment()

    }

    private fun setupRecyclerView(){
        historyitemAdapter = MyShipmentAdapter()
        recycler_view_delivered.apply {
            adapter = historyitemAdapter.also {
                it.setOnItemClickListener{ wbData ->
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Lacak Resi Ini?")
                        .setIcon(R.drawable.ic_parcel)
                        .setMessage(wbData.waybillNumber)
                        .setPositiveButton("YA"){ dialog, which ->
                            val bundle = Bundle().apply {
                                putString("waybillNumber", wbData.waybillNumber)
                                putString("courier", wbData.courier.code)
                                putString("waybillName", wbData.waybillName)
                            }
                            findNavController().navigate(R.id.action_myShipmentFragment_to_trackWaybillFragment,bundle)
                        }
                        .setNegativeButton("CANCEL"){dialog, which ->
                        }
                        .show()
                }
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getDeliveredShipment(){
        viewModel.getSavedWaybill("DELIVERED").observe(viewLifecycleOwner, Observer {
            recycler_view_delivered.apply {
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
                view?.rootView?.let { Snackbar.make(it, "Successfully remove waybill data", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveWaybill(waybill)
                    }.show()
                } }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recycler_view_delivered)
        }


    }

}
