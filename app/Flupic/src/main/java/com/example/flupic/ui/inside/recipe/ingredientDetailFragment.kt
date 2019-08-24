package com.example.flupic.ui.inside.recipe


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.flupic.R
import com.example.flupic.databinding.FragmentIngredientDetailBinding
import com.example.flupic.util.IngredientListAdapter
import com.example.flupic.viewmodels.AddViewModel
import com.example.flupic.viewmodels.DetailViewModel


class ingredientDetailFragment : Fragment() {

    lateinit var binding: FragmentIngredientDetailBinding

    private val recipeDetailViewModel : DetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!) .get(
            DetailViewModel::class.java)
    }

    private val adapter = IngredientListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIngredientDetailBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = adapter

        recipeDetailViewModel.recipe.observe(this, Observer {
            adapter.data = it.ingredients
        })

        return binding.root
    }


}
