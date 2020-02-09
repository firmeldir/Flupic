package com.example.flupic.ui.auth


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.flupic.R

import com.example.flupic.databinding.FragmentSignInBinding
import com.example.flupic.ui.base.BaseAuthFragmentChild
import com.example.flupic.util.*
import com.example.flupic.util.handler.PhoneNumberVerificationHandler
import javax.inject.Inject


class SignInFragment : BaseAuthFragmentChild(), ProgressView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var verificationHandler: PhoneNumberVerificationHandler

    private lateinit var binding: FragmentSignInBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        setupUI()

        return binding.root
    }

    private fun setupUI(){ //TODO setupPrivacyDisclosures()

        binding.phoneNumberInput.setImeOnDoneListener { onNextFlowStep() }
        binding.nextButton.setOnClickListener { onNextFlowStep() }
        binding.countryCodePicker.setOnClickListener { showInputErrorMessage(null) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            binding.phoneNumberInput.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
    }

    private fun onNextFlowStep(){
        var number = binding.phoneNumberInput.getValidPhoneNumberOr()
        number = number?.formatPhoneNumberUsingCountryCode(binding.countryCodePicker.selectedCountryCode)

        if(number == null)
            showInputErrorMessage(getString(R.string.invalid_phone_number))
        else verificationHandler.verifyPhoneNumber(number, false)
    }

    private fun showInputErrorMessage(message: String?){
        binding.phoneNumberInput.error = message
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        verificationHandler =  viewModelProvider(viewModelFactory)
    }

    private fun inject() = with(authFragment.authComponent){
        this.inject(this@SignInFragment)
    }

    override fun showProgress() {
        binding.nextButton.isEnabled = false
    }

    override fun hideProgress() {
        binding.nextButton.isEnabled = true
    }
}
