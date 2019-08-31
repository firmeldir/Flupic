package com.example.flupic.ui.inside


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.example.flupic.R
import com.example.flupic.TAG
import com.example.flupic.databinding.FragmentDetailBinding
import com.example.flupic.di.ViewModelProviderFactory
import com.example.flupic.util.AddPageListener
import com.example.flupic.util.AddPecipeAdapter
import com.example.flupic.util.RecipePagerAdapter
import com.example.flupic.viewmodels.DetailViewModel
import com.example.flupic.viewmodels.EditViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_authentication.*
import javax.inject.Inject


class detailFragment : DaggerFragment() {

    lateinit var binding: FragmentDetailBinding

    //Injection of ViewModel
    @Inject
    lateinit var injectFactory: ViewModelProviderFactory

    private val detailViewModel : DetailViewModel by lazy {
        ViewModelProviders.of(this, injectFactory).get(DetailViewModel::class.java)
    }

    private lateinit var accessId : String
    private lateinit var authorId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = android.transition.TransitionInflater.from(context).inflateTransition(R.transition.custom_move_trans)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        accessId = detailFragmentArgs.fromBundle(arguments!!).accesId
        authorId = detailFragmentArgs.fromBundle(arguments!!).authorId

        detailViewModel.loadInfoById(accessId, authorId)

        setupViewPager()

        startObservingViewModel()

        binding.backButton.setOnClickListener { navBack() }

        return binding.root
    }

    fun navBack() = findNavController().popBackStack()

    fun startObservingViewModel(){

        detailViewModel.dish.observe(this, Observer {
            binding.fireDish = it
            binding.fragmentDetailInside.fireDish = it
        })

        detailViewModel.recipe.observe(this, Observer {
            binding.fragmentDetailInside.cookingTime.text = "${it.cookTimeMin} minutes"
            binding.fragmentDetailInside.persPerService.text = "${it.peoplePerServing} servings"
        })

    }

    fun setupViewPager(){
        binding.fragmentDetailInside.pager.apply {
            adapter = RecipePagerAdapter(childFragmentManager)
        }
    }
}
