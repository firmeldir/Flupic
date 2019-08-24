package com.example.flupic.ui.inside.recipe


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.flupic.R
import com.example.flupic.databinding.FragmentRecipeDetailBinding
import com.example.flupic.util.RecipeListAdapter
import com.example.flupic.viewmodels.DetailViewModel


class recipeDetailFragment : Fragment() {

    lateinit var binding: FragmentRecipeDetailBinding

    private val recipeDetailViewModel : DetailViewModel by lazy {
        ViewModelProviders.of(parentFragment!!) .get(
            DetailViewModel::class.java)
    }

    private val adapter = RecipeListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = adapter

        recipeDetailViewModel.recipe.observe(this, Observer {
            adapter.data = it.recipe
        })

        return binding.root
    }


}
