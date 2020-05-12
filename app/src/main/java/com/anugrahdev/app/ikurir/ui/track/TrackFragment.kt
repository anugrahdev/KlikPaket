package com.anugrahdev.app.ikurir.ui.track

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.anugrahdev.app.ikurir.R

class TrackFragment : Fragment() {

    companion object {
        fun newInstance() = TrackFragment()
    }

    private lateinit var viewModel: TrackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.track_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TrackViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
