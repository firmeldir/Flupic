package com.example.flupic.ui.auth


import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.example.flupic.R
import com.example.flupic.databinding.FragmentAuthBinding
import com.example.flupic.databinding.FragmentConfirmationBinding
import com.example.flupic.databinding.FragmentSignInBinding
import com.example.flupic.result.Result
import com.example.flupic.ui.base.BaseAuthFragmentChild
import com.example.flupic.util.BucketedTextChangeListener
import com.example.flupic.util.ProgressView
import com.example.flupic.util.handler.PhoneNumberVerificationHandler
import com.example.flupic.util.handler.PhoneProviderResponseHandler
import com.example.flupic.util.handler.PhoneProviderResponseHandler.Companion.VERIFICATION_CODE_LENGTH
import com.example.flupic.util.viewModelProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ConfirmationFragment : BaseAuthFragmentChild(), ProgressView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var verificationHandler: PhoneNumberVerificationHandler
    private lateinit var providerResponseHandler: PhoneProviderResponseHandler

    private lateinit var binding: FragmentConfirmationBinding

    private lateinit var phoneNumber: String
    private val args: ConfirmationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false)

        setupUI()

        setupDelayTimerObserver()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        verificationHandler = viewModelProvider(viewModelFactory)
        providerResponseHandler = viewModelProvider(viewModelFactory)

        phoneNumber = args.phoneNumber
    }

    override fun onStart() {
        super.onStart()
        focusConfirmationCodeEditText()
    }




    //  *  *  *  *  *

    private fun setupDelayTimerObserver(){

        binding.resendButton.setOnClickListener {
            verificationHandler.verifyPhoneNumber(phoneNumber, true)
        }

        verificationHandler.delayBeforeResend.observe(this, Observer {
            if (it == 0L)
                binding.resendButton.visibility = View.VISIBLE
            else binding.resendButton.visibility = View.GONE

            binding.resendText.text = String.format(getString(R.string.resend_code), it)
        })
    }

    private fun setupUI(){
        setupConfirmationCodeEditText()
        setupEditPhoneNumberTextView()

        binding.nextButton.setOnClickListener { submitCode() }
    }

    private fun setupConfirmationCodeEditText(){
        binding.confirmationCode.addTextChangedListener(BucketedTextChangeListener(
            binding.confirmationCode, "-", VERIFICATION_CODE_LENGTH,
                object : BucketedTextChangeListener.ContentChangeCallback{
                    override fun whenComplete() { submitCode() }
                    override fun whileIncomplete() {}
                }
            ))

        providerResponseHandler.operationResult.observe(this, Observer {
            if(it.getContentIfNotHandled() is Result.Error){ binding.confirmationCode.setText("") }
        })
    }

    private fun setupEditPhoneNumberTextView(){

    }

    private fun focusConfirmationCodeEditText(){
        binding.confirmationCode.requestFocus()

        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(binding.confirmationCode, 0)
    }

    private fun submitCode(){
        verificationHandler.submitVerificationCode(phoneNumber, binding.confirmationCode.text.toString())
    }

    override fun showProgress() { binding.nextButton.isEnabled = false }

    override fun hideProgress() { binding.nextButton.isEnabled = true }

    private fun inject() = with(authFragment.authComponent){
        this.inject(this@ConfirmationFragment)
    }
}
