package com.example.flupic.ui.test


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.flupic.BaseApplication

import com.example.flupic.R
import com.example.flupic.databinding.FragmentProfileUserBinding
import com.example.flupic.databinding.FragmentTestBinding
import com.example.flupic.ui.profile.UserProfileViewModel
import javax.inject.Inject


class TestFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestBinding.inflate(inflater, container, false)

        return binding.root
    }


    private fun inject() = with((activity!!.application as BaseApplication).appComponent){
        this.inject(this@UserProfileFragment)
    }
}
