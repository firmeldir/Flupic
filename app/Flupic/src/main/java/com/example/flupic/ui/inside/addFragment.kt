package com.example.flupic.ui.inside

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.flupic.R

import com.example.flupic.databinding.FragmentAddBinding
import com.example.flupic.di.ViewModelProviderFactory
import com.example.flupic.domain.FireDish
import com.example.flupic.domain.FireRecipe
import com.example.flupic.util.AddPageListener
import com.example.flupic.util.AddPecipeAdapter
import com.example.flupic.viewmodels.AddViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add.*
import javax.inject.Inject


class addFragment : DaggerFragment() {

    lateinit var binding: FragmentAddBinding

    //Injection of ViewModel
    @Inject
    lateinit var injectFactory: ViewModelProviderFactory

    private val addViewModel : AddViewModel by lazy {
        ViewModelProviders.of(this, injectFactory).get(
            AddViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        setupViewPager()

        addViewModel.isDone.observe(this, Observer {
            if (it == true){
                navBack()
            }
        })

        binding.doneButton.setOnClickListener {

            binding.shimmer.startShimmer()

            var name: String = ""
            var description: String = ""
            var peoplePerService: Int = 0
            var cookingTime: Int = 0

            val adapter = binding.viewPager2.adapter

            if(adapter is AddPecipeAdapter){
                val infoFragBinding = adapter.fragment2.binding
                name = infoFragBinding.dishNameInput.text.toString()
                description = infoFragBinding.dishDescriptionInput.text.toString()
                if (!infoFragBinding.peoplePerServiceInput.text.toString().isEmpty()){
                    peoplePerService = infoFragBinding.peoplePerServiceInput.text.toString().toInt()
                }
                if (!infoFragBinding.timeForCookingInput.text.toString().isEmpty()){
                    cookingTime = infoFragBinding.timeForCookingInput.text.toString().toInt()
                }
            }

            val fireDish = FireDish(name, description = description)
            val fireRecipe = FireRecipe(cookingTime, peoplePerService)

            binding.doneButton.isEnabled = false

            addViewModel.postDish(fireDish, fireRecipe)
        }

        return binding.root
    }


    fun navBack(){ findNavController().popBackStack() }

    fun setupViewPager(){

        binding.viewPager2.apply {
            adapter = AddPecipeAdapter(childFragmentManager)
            addOnPageChangeListener(AddPageListener(binding.addMotionLayout))

            binding.nextButton.setOnClickListener {
                setCurrentItem(this.currentItem + 1, true)
            }

            binding.prevButton.setOnClickListener {
                setCurrentItem(this.currentItem - 1, true)
            }
        }
    }
}
