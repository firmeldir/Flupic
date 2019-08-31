package com.example.flupic.ui.dialogue


import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.flupic.R
import com.example.flupic.TAG
import com.example.flupic.databinding.FragmentBusinessLocationDialogueBinding
import com.example.flupic.viewmodels.AddViewModel
import com.example.flupic.viewmodels.EditViewModel
import dagger.android.support.DaggerAppCompatDialogFragment

const val BIS_DIA_LOC_ID = "businessLocationDialogueFragment"

class businessLocationDialogueFragment : DaggerAppCompatDialogFragment() {

    interface LocationListener {
        fun onSetLocation()
    }

    private lateinit var binding: FragmentBusinessLocationDialogueBinding

    private val editViewModel : EditViewModel by lazy {
        ViewModelProviders.of(parentFragment!!) .get(
            EditViewModel::class.java)
    }

    private lateinit var locationListener: LocationListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessLocationDialogueBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.noButton.setOnClickListener{ onNoClicked() }

        binding.yesButton.setOnClickListener { onYesClicked() }

        editViewModel.lastLocation.observe(this, Observer {
            binding.location = it
        })

        return binding.root
    }

    fun onDone() = dismiss()

    private fun onYesClicked(){
        //Block until done
        binding.shimmer.startShimmer()
        binding.yesButton.isEnabled = false
        locationListener.onSetLocation()
    }

    private fun onNoClicked() = dismiss()

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (parentFragment is LocationListener) {
            locationListener = parentFragment as LocationListener
        }
    }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
