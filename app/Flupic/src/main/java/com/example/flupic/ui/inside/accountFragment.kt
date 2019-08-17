package com.example.flupic.ui.inside


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.example.flupic.R
import com.example.flupic.databinding.FragmentAccountBinding
import com.example.flupic.util.PublPagerAdapter
import com.example.flupic.viewmodels.ProfileViewModel


class accountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding

    private val profileViewModel : ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(
            ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        setupUI()

        binding.user = profileViewModel

        return binding.root
    }

    fun setupUI(){
        binding.include.pager.adapter = PublPagerAdapter(childFragmentManager)
    }
}
