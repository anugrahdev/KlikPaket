package com.anugrahdev.app.ikurir.ui.myshipment

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.item_savedawb.*
import kotlinx.android.synthetic.main.onprocess_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class OnProcessFragment : Fragment() , KodeinAware {

    override val kodein by kodein()
    val factory: WaybillViewModelFactory by instance<WaybillViewModelFactory>()
    private lateinit var viewModel: WaybillViewModel
    lateinit var historyitemAdapter: MyShipmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.onprocess_fragment, container, false)


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(WaybillViewModel::class.java)
        setupRecyclerView()
        getOnProcessShipment()


    }

    private fun setupRecyclerView(){
        historyitemAdapter = MyShipmentAdapter()
        recycler_view_onprocess.apply {
            adapter = historyitemAdapter.also {
                it.setOnItemClickListener{ wbData ->
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.track_this))
                        .setIcon(R.drawable.ic_parcel)
                        .setMessage(wbData.waybillNumber)
                        .setPositiveButton("YES"){ dialog, which ->
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

    private fun getOnProcessShipment(){
        viewModel.getSavedWaybill("ON PROGRESS").observe(viewLifecycleOwner, Observer {
            if  (it.isEmpty()){
                cv_notfound.visibility = View.VISIBLE
            }
            recycler_view_onprocess.apply {
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
                if (waybill.history){
                    waybill.saved = false
                    viewModel.updateWaybill(waybill)

                }else if(!waybill.history){
                    viewModel.deleteSavedWaybill(waybill)
                }
                view?.rootView?.let { Snackbar.make(it, "Successfully remove waybill data", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        if (!waybill.history){
                            viewModel.saveWaybill(waybill)
                        }else{
                            waybill.saved = true
                            viewModel.updateWaybill(waybill)
                        }
                    }.show()
                } }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recycler_view_onprocess)
        }


    }


}
