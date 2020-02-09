package com.example.flupic.ui.auth


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.flupic.R
import com.example.flupic.databinding.FragmentSignInBinding
import com.example.flupic.databinding.FragmentStartBinding
import com.example.flupic.ui.base.BaseAuthFragmentChild
import javax.inject.Inject


class StartFragment : BaseAuthFragmentChild() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentStartBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()
        binding = FragmentStartBinding.inflate(inflater, container, false)

        setupUI()

        return binding.root
    }

    private fun setupUI(){
        binding.startButton.setOnClickListener { navigateToSignInFragment() }
    }

    private fun navigateToSignInFragment(){ findNavController().navigate(R.id.action_startFragment_to_signInFragment) }

    private fun inject() = with(authFragment.authComponent){
        this.inject(this@StartFragment)
    }
}
