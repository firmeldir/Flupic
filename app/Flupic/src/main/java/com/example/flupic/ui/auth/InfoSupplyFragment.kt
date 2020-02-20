package com.example.flupic.ui.auth


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.flupic.BaseApplication
import com.example.flupic.R

import com.example.flupic.databinding.FragmentInfoSupplyBinding
import com.example.flupic.result.ActivityRequestEventObserver
import com.example.flupic.result.EventObserver
import com.example.flupic.util.getValidUsernameOr
import com.example.flupic.util.setUpSnackbar
import com.example.flupic.util.view.SnackbarMessageManager
import com.example.flupic.util.viewModelProvider
import javax.inject.Inject


class InfoSupplyFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    private lateinit var infoSupplyViewModel: InfoSupplyViewModel

    private lateinit var binding: FragmentInfoSupplyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()

        infoSupplyViewModel = viewModelProvider(viewModelFactory)

        binding = FragmentInfoSupplyBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = infoSupplyViewModel
            }

        setupUI()

        setupObservers()

        return binding.root
    }

    private fun setupUI(){
        binding.nextButton.setOnClickListener(::onNext)

        setUpSnackbar(infoSupplyViewModel.snackbarMessage, binding.snackbar, snackbarMessageManager)
    }

    private fun onNext(view: View){
        val username = binding.usernameInput.getValidUsernameOr()
        if(username == null){
            infoSupplyViewModel.showSnackBarMessage(R.string.not_valid_value)
            return
        }
        infoSupplyViewModel.onNextClick(username)
    }

    private fun setupObservers(){
        infoSupplyViewModel.performPickPhotoEvent.observe(this, ActivityRequestEventObserver(this))

        infoSupplyViewModel.navigateToProfileAction.observe(this, EventObserver {
            navigateToProfile()
        })
    }

    private fun navigateToProfile(){
        findNavController().navigate(R.id.profileFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        infoSupplyViewModel.onPickPhotoResult(requestCode, resultCode, data)
    }

    private fun inject() = with((activity!!.application as BaseApplication).appComponent){
        this.inject(this@InfoSupplyFragment)
    }
}
