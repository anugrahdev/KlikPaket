package com.anugrahdev.app.klikPaket.ui.shipmentcost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.adapter.CostAdapter
import com.anugrahdev.app.klikPaket.data.network.Resource
import com.anugrahdev.app.klikPaket.utils.hide
import com.anugrahdev.app.klikPaket.utils.show
import com.anugrahdev.app.klikPaket.utils.snackbar
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cost.*
import kotlinx.coroutines.*
import java.util.*


@AndroidEntryPoint
class CostFragment : Fragment(){
    private val viewModel: CostViewModel by viewModels()
    private var cityId: Int = 0
    private var districtId: Int = 0
    private var weight: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cost, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        weight = et_weight.text.toString().toInt()
        spinner_courier.setTitle("Select Courier")
        val loadingstyle: Sprite = WanderingCubes()
        spin_kit_progress_bar.setIndeterminateDrawable(loadingstyle)
        reset()
        processOriginData()
        processDestinationData()
        processWeight()
        processCalculateCost()

        ic_back.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_costFragment_to_homeFragment,
                null
            )
        )


    }

    private fun reset() {
        btn_reset.setOnClickListener {
            actv_destination.setText("")
            actv_origin.setText("")
            et_weight.setText("1")

        }
    }

    private fun processCalculateCost() {

        btn_calculate_cost.setOnClickListener {
            if (actv_origin.text.isNotEmpty() && actv_destination.text.isNotEmpty() && weight != 0) {
                var courier = spinner_courier.selectedItem.toString()
                if (courier == "Semua") {
                    courier = "jne,jnt,ic_sicepat,tiki,lion,alfatrex,pcp,sap"
                }
                viewModel.postShippingCost(cityId, districtId, weight * 1000,
                    courier.toLowerCase(Locale.ROOT))
            } else if(cityId == 0 || districtId == 0) {
                constraintLayout.snackbar(getString(R.string.input_selected))
            }else{
                constraintLayout.snackbar(getString(R.string.form_not_filled))
            }
        }

        viewModel.shippingCost.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { data ->
                        recycler_view_shippingcost.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = CostAdapter(data)
                        }
                        if(data.isEmpty()){
                            constraintLayout.snackbar(getString(R.string.not_found))
                        }
                    }
                    stopLoading()
                }
                is Resource.Loading -> {
                    startLoading()
                }
                is Resource.Error -> {
                    response.message?.let { error ->
                        constraintLayout.snackbar(error)
                    }
                    stopLoading()
                }
            }
        })
    }

    private fun processOriginData() {
        lateinit var mCitiesAdapter: ArrayAdapter<String>
        val citieslist: MutableList<String> = mutableListOf()
        var job: Job? = null
        actv_origin.addTextChangedListener { editable ->
            job?.cancel()
            job = CoroutineScope(Dispatchers.IO).launch {
                delay(300)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getCities(editable.toString())
                    }
                }
            }
        }


        viewModel.cities.observe(viewLifecycleOwner, Observer { response ->


            when (response) {
                is Resource.Success -> {
                    response.data?.let { data ->
                        citieslist.clear()
                        for (element in data) {
                            citieslist.addAll(arrayListOf(element.name + ", " + element.type))
                        }
                        mCitiesAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            citieslist
                        )
                        actv_origin.threshold = 3
                        actv_origin.setOnItemClickListener { _, _, position, _ ->
                            cityId = data[position].id
                        }
                        actv_origin.setAdapter(mCitiesAdapter)
                        if (data.isNotEmpty()) {
                            cityId = data[0].id
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { error ->
                        constraintLayout.snackbar("Error occured : $error")
                    }
                }
            }

        })
    }

    private fun processDestinationData() {
        lateinit var mDistrictsAdapter: ArrayAdapter<String>
        val districtslist: MutableList<String> = mutableListOf()
        var job: Job? = null
        actv_destination.addTextChangedListener { editable ->
            job?.cancel()
            job = CoroutineScope(Dispatchers.IO).launch {
                delay(300)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getDistricts(editable.toString())
                    }
                }
            }
        }


        viewModel.districts.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let { data ->
                        districtslist.clear()
                        for (element in data) {
                            districtslist.addAll(arrayListOf(element.name + ", " + element.city.name))
                        }
                        mDistrictsAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            districtslist
                        )
                        actv_destination.threshold = 3
                        actv_origin.setOnItemClickListener { _, _, position, _ ->
                            districtId = data[position].id
                        }
                        actv_destination.setAdapter(mDistrictsAdapter)
                        if (data.isNotEmpty()) {
                            districtId = data[0].id
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { error ->
                        constraintLayout.snackbar("Error occured : $error")
                    }
                }
            }

        })
    }

    private fun processWeight() {
        btn_minus.setOnClickListener {
            if (et_weight.text.toString().toInt() > 1) {
                val number: Int = et_weight.text.toString().toInt()
                et_weight.setText((number - 1).toString())
            }
        }

        btn_plus.setOnClickListener {
            val number: Int = et_weight.text.toString().toInt()
            et_weight.setText((number + 1).toString())
        }
    }

    private fun startLoading() {
        spin_kit_progress_bar.show()
    }

    private fun stopLoading() {
        spin_kit_progress_bar.hide()
    }


}
