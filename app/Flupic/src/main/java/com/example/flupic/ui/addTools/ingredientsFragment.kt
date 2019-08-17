package com.example.flupic.ui.addTools


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.flupic.R
import com.example.flupic.TAG
import com.example.flupic.databinding.FragmentIngredientsBinding
import com.example.flupic.util.IngredientListAdapter
import com.example.flupic.viewmodels.AddViewModel
import com.example.flupic.viewmodels.ProfileViewModel


class ingredientsFragment : Fragment() {

    lateinit var binding: FragmentIngredientsBinding

    private val addViewModel : AddViewModel by lazy {
        ViewModelProviders.of(parentFragment!!) .get(
            AddViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        binding.addIng.setOnClickListener { addViewModel.addIngredient(binding.ingrNameInput.text.toString(),binding.ingCountInput.text.toString()) }

        setupRecyclerview()

        return binding.root
    }


    fun setupRecyclerview(){

        val adapter = IngredientListAdapter()
        binding.recyclerView.adapter = adapter

        addViewModel.ingredient.observe(this, Observer {
            adapter.data = it
        })
    }
}
