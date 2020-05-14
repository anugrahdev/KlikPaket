package com.anugrahdev.app.ikurir.ui.shipmentcost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anugrahdev.app.ikurir.R
import com.anugrahdev.app.ikurir.adapter.CostAdapter
import com.anugrahdev.app.ikurir.utils.hide
import com.anugrahdev.app.ikurir.utils.show
import com.anugrahdev.app.ikurir.utils.snackbar
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import kotlinx.android.synthetic.main.cost_fragment.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class CostFragment : Fragment(),KodeinAware {
    override val kodein by kodein()
    private lateinit var viewModel: CostViewModel
    private val factory: CostViewModelFactory by instance<CostViewModelFactory>()
    private var adapter:CostAdapter = CostAdapter(emptyList())
    var cityId:Int = 0
    var districtId:Int = 0
    var weight:Int = 0
    companion object {
        fun newInstance() = CostFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cost_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,factory).get(CostViewModel::class.java)
        weight = et_weight.text.toString().toInt()

        val loadingstyle: Sprite = WanderingCubes()
        spin_kit_progress_bar.setIndeterminateDrawable(loadingstyle)
        reset()
        processOriginData()
        processDestinationData()
        processWeight()
        processCalculateCost()



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
            startLoading()
            if (cityId!=0 && districtId !=0 && weight!=0){
                startLoading()
                var courier = spinner_courier.selectedItem.toString()
                if (courier=="Semua") { courier = "jne,jnt,sicepat,tiki,lion,alfatrex,pcp,sap" }
                viewModel.postShippingCost(cityId, districtId, weight*1000, courier.toLowerCase())
            }else{
                constraintLayout.snackbar("Semua form harus di isi !")
            }
        }

        viewModel.shippingcost.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                constraintLayout.snackbar("Courier Not Found!")

            }
            recycler_view_shippingcost.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = CostAdapter(it)
                stopLoading()
            }
        })
    }

    private fun processOriginData(){
        lateinit var mCitiesAdapter:ArrayAdapter<String>
        var citieslist : MutableList<String> = mutableListOf()
        var job: Job? = null
        actv_origin.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.getCities(editable.toString())
                    }
                }
            }
        }


        viewModel.cities.observe(viewLifecycleOwner, Observer {
            citieslist.clear()
            for(element in it){
                citieslist.addAll(arrayListOf(element.name+", "+element.type))
            }
            mCitiesAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, citieslist)
            actv_origin.threshold=3
            actv_origin.setOnItemClickListener { parent, view, position, id ->
                cityId = it[position].id
            }
            actv_origin.setAdapter(mCitiesAdapter)
            if(it.isNotEmpty()){
                cityId = it[0].id
            }

        })
    }
    private fun processDestinationData(){
        lateinit var mDistrictsAdapter:ArrayAdapter<String>
        var districtslist : MutableList<String> = mutableListOf()
        var job: Job? = null
        actv_destination.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.getDistricts(editable.toString())
                    }
                }
            }
        }


        viewModel.districts.observe(viewLifecycleOwner, Observer {
            districtslist.clear()
            for(element in it){
                districtslist.addAll(arrayListOf(element.name+", "+element.city.name))
            }
            mDistrictsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, districtslist)
            actv_destination.threshold=3
            actv_origin.setOnItemClickListener { parent, view, position, id ->
                districtId = it[position].id
            }
            actv_destination.setAdapter(mDistrictsAdapter)
            if(it.isNotEmpty()){
                districtId = it[0].id
            }

        })
    }
    private fun processWeight(){
        btn_minus.setOnClickListener {
            if (et_weight.text.toString().toInt()>1){
                var number: Int = et_weight.getText().toString().toInt()
                et_weight.setText((number-1).toString())
            }
        }

        btn_plus.setOnClickListener {
            var number: Int = et_weight.getText().toString().toInt()
            et_weight.setText((number+1).toString())
        }
    }

    private fun startLoading(){
        spin_kit_progress_bar.show()
        btn_calculate_cost.text = "Loading..."
    }

    private fun stopLoading(){
        spin_kit_progress_bar.hide()
        btn_calculate_cost.text = "HITUNG ONGKIR"

    }


}
