package com.example.flupic.ui.inside


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.flupic.R
import com.example.flupic.TAG
import com.example.flupic.databinding.FragmentAccountBinding
import com.example.flupic.di.ViewModelProviderFactory
import com.example.flupic.util.PublPagerAdapter
import com.example.flupic.viewmodels.ProfileViewModel
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import javax.inject.Inject


class accountFragment : DaggerFragment() {

    lateinit var binding: FragmentAccountBinding

    //Injection of ViewModel
    @Inject
    lateinit var injectFactory: ViewModelProviderFactory

    private val profileViewModel : ProfileViewModel by lazy {
        ViewModelProviders.of(this, injectFactory).get(
            ProfileViewModel::class.java)
    }

    private lateinit var profileUID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        profileUID = accountFragmentArgs.fromBundle(arguments!!).userUID

        setupListener()
        setupUI()

        return binding.root
    }

    private fun changeSub(){
        profileViewModel.subscribe()
    }

    private fun setupListener(){
        if(profileUID == "0"){
            profileViewModel.listenUser()
        }else{
            profileViewModel.listenUser(profileUID)
        }
    }

    private fun setupUI(){
        binding.user = profileViewModel

        if(profileUID == "0" || profileUID == profileViewModel.userUID){
            binding.subscribe.visibility = View.GONE
            binding.include.pager.adapter = PublPagerAdapter(childFragmentManager)
        }else{
            binding.subscribe.setOnClickListener { changeSub() }
            binding.include.pager.adapter = PublPagerAdapter(childFragmentManager, profileUID)
        }

        profileViewModel.isSubscribed.observe(this, Observer {
            if(it){
                binding.subscribe.setBackgroundColor(ContextCompat.getColor(context!!,R.color.secondaryColor))
                binding.subscribe.text = resources.getString(R.string.unsubscribe)
                binding.subscribe.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_unsubscribe)
            }else{
                binding.subscribe.setBackgroundColor(ContextCompat.getColor(context!!,R.color.primaryColor))
                binding.subscribe.text = resources.getString(R.string.subscribe)
                binding.subscribe.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_subscribe)
            }
        })
    }
}
