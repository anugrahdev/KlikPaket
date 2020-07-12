package com.anugrahdev.app.klikPaket.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.list.listItemsSingleChoice

import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.preferences.SettingsPref
import com.anugrahdev.app.klikPaket.ui.AboutActivity
import com.anugrahdev.app.klikPaket.ui.trackwaybill.WaybillViewModel
import com.anugrahdev.app.klikPaket.utils.convertCountryCode
import com.anugrahdev.app.klikPaket.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setting.*

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private val viewModel: WaybillViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ic_back.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_settingFragment_to_homeFragment, null))

        val languageList = listOf("Bahasa Indonesia", "English")
        tv_selectedLanguage.setText(convertCountryCode(SettingsPref.language))
        setting_language.setOnClickListener {
            MaterialDialog(requireContext()).show {
                var init = 0
                for (i in languageList.indices){
                    if (convertCountryCode(languageList[i]) == SettingsPref.language){
                        init = i
                    }
                }
                listItemsSingleChoice(items = languageList, initialSelection = init){ _, index, _ ->
                    var lang = convertCountryCode(languageList[index])
                    activity?.tv_selectedLanguage?.setText(lang)
                    SettingsPref.language = lang
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
            Intent(requireContext(), AboutActivity::class.java).also{
                startActivity(it)
            }
        }

        setting_ekspedisi?.setOnClickListener {
            MaterialDialog(requireContext()).show {
                customView(R.layout.layout_courierinfo,scrollable = true)
            }
        }


    }


}
