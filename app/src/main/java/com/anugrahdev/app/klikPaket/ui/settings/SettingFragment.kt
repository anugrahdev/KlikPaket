package com.anugrahdev.app.klikPaket.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.list.listItemsSingleChoice

import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.preferences.PreferenceProvider
import com.anugrahdev.app.klikPaket.ui.trackwaybill.WaybillViewModel
import com.anugrahdev.app.klikPaket.ui.trackwaybill.WaybillViewModelFactory
import com.anugrahdev.app.klikPaket.utils.snackbar
import kotlinx.android.synthetic.main.setting_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SettingFragment : Fragment() , KodeinAware {

    override val kodein by kodein()
    private val factory: WaybillViewModelFactory by instance<WaybillViewModelFactory>()
    private lateinit var viewModel: WaybillViewModel

    private lateinit var prefs:PreferenceProvider
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,factory).get(WaybillViewModel::class.java)

        ic_back.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_settingFragment_to_homeFragment, null))

        val languageList = listOf("Bahasa Indonesia", "English")
        prefs = PreferenceProvider(requireContext())
        tv_selectedLanguage.setText(prefs.getLanguage())
        setting_language.setOnClickListener {
            MaterialDialog(requireContext()).show {
                var init = 0
                for (i in languageList.indices){
                    if (languageList[i] == prefs.getLanguage()){
                        init = i
                    }
                }
                listItemsSingleChoice(items = languageList, initialSelection = init){ _, index, _ ->
                    activity?.tv_selectedLanguage?.setText(languageList[index])
                    prefs.setLanguage(languageList[index])
                    activity?.recreate()
                }
            }
        }

        setting_clearhistory.setOnClickListener {
            MaterialDialog(requireContext()).show {
                title(text="Konfirmasi")
                message(text="Yakin Ingin Menghapus Semua Riwayat Pencarian Resi ?")
                positiveButton(text="YA"){
                    viewModel.clearHistory()
                    activity?.root_layout?.snackbar("Berhasil Membersihkan Riwayat Pencarian")
                }
                negativeButton(text="CANCEL")
            }
        }

        setting_about.setOnClickListener {
            MaterialDialog(requireContext()).show {
                customView(R.layout.layout_about)
            }
        }

        setting_ekspedisi?.setOnClickListener {
            MaterialDialog(requireContext()).show {
                customView(R.layout.layout_courierinfo,scrollable = true)
            }
        }


    }



}
