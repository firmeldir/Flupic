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
import com.example.flupic.databinding.FragmentRecipeAddBinding
import com.example.flupic.util.IngredientListAdapter
import com.example.flupic.util.RecipeListAdapter
import com.example.flupic.viewmodels.AddViewModel


class recipeAdd : Fragment() {

    lateinit var binding: FragmentRecipeAddBinding

    private val addViewModel : AddViewModel by lazy {
        ViewModelProviders.of(parentFragment!!) .get(
            AddViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeAddBinding.inflate(inflater, container, false)

        binding.addRecipeItem.setOnClickListener {
            addViewModel.addRecipeItem(binding.stepNameInput.text.toString(),
            binding.stepDescriptionInput.text.toString()) }

        setupRecyclerview()

        return binding.root
    }

    fun setupRecyclerview(){

        val adapter = RecipeListAdapter()
        binding.recycleView.adapter = adapter

        addViewModel.recipe.observe(this, Observer {
            adapter.data = it
        })
    }
}
