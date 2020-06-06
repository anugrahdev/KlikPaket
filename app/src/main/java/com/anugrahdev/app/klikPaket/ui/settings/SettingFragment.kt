package com.anugrahdev.app.klikPaket.ui.settings

import android.content.Intent
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
import com.anugrahdev.app.klikPaket.ui.AboutActivity
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
        tv_selectedLanguage.setText(convertCountryCode(prefs.getLanguage().toString()))
        setting_language.setOnClickListener {
            MaterialDialog(requireContext()).show {
                var init = 0
                for (i in languageList.indices){
                    if (convertCountryCode(languageList[i]) == prefs.getLanguage()){
                        init = i
                    }
                }
                listItemsSingleChoice(items = languageList, initialSelection = init){ _, index, _ ->
                    var lang = convertCountryCode(languageList[index])
                    activity?.tv_selectedLanguage?.setText(lang)
                    prefs.setLanguage(lang)
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

    private fun convertCountryCode(key: String): String{
        var lang=""
        when(key){
            "Bahasa Indonesia" -> lang="id"
            "English" -> lang="en"
            "en" -> lang="English"
            "id" -> lang="Bahasa Indonesia"
        }
        return lang
    }


}
