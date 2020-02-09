package com.example.flupic.ui.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.flupic.BaseApplication

import com.example.flupic.R
import com.example.flupic.databinding.FragmentInfoSupplyBinding
import com.example.flupic.databinding.FragmentProfileBinding
import com.example.flupic.databinding.FragmentStartBinding
import com.example.flupic.util.viewModelProvider
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var auth: FirebaseAuth


    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()

        profileViewModel = viewModelProvider(viewModelFactory)

        binding = FragmentProfileBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = profileViewModel
            }
        
        return binding.root
    }

    private fun inject() = with((activity!!.application as BaseApplication).appComponent){
        this.inject(this@ProfileFragment)
    }
}
