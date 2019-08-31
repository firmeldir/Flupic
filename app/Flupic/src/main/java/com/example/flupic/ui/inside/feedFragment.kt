package com.example.flupic.ui.inside


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController

import com.example.flupic.R
import com.example.flupic.TAG
import com.example.flupic.databinding.FragmentFeedBinding
import com.example.flupic.di.ViewModelProviderFactory
import com.example.flupic.util.adapters.FeedAction
import com.example.flupic.util.adapters.FeedRecyclerViewAdapter
import com.example.flupic.util.adapters.OwnRecyclerViewAdapter
import com.example.flupic.viewmodels.EditViewModel
import com.example.flupic.viewmodels.FeedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.feed_item.*
import kotlinx.android.synthetic.main.fragment_authentication.*
import javax.inject.Inject

const  val SEARCH_LIMIT = 5

//todo recycler view
class feedFragment : DaggerFragment() {

    lateinit var binding: FragmentFeedBinding

    //Injection of ViewModel
    @Inject
    lateinit var injectFactory: ViewModelProviderFactory

    @Inject
    lateinit var auth: FirebaseAuth

    private val feedViewModel : FeedViewModel by lazy {
        ViewModelProviders.of(this, injectFactory).get(
            FeedViewModel::class.java)
    }

    private val adapter: FeedRecyclerViewAdapter by lazy {
        FeedRecyclerViewAdapter(auth.uid!!
            , ContextCompat.getDrawable(context!!, R.drawable.ic_favorite_black_24dp)
            , ContextCompat.getDrawable(context!!,R.drawable.ic_favorite_border)
        ) { d: String, a:String, feedAction: FeedAction ->

            if(feedAction == FeedAction.SELECT){
                val extras = FragmentNavigatorExtras(
                    mainImages to "imageDetail"
                )
                val action =  feedFragmentDirections.actionFeedFragmentToDetailFragment(d, a)
                findNavController().navigate(action,extras)
            }else{
                feedViewModel.like(d, a)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)

        setupUI()

        return binding.root
    }

    private fun setupUI(){
        binding.recyclerView.adapter = adapter

        feedViewModel.feed.observe(this, Observer {
            adapter.data = it
            Log.i(TAG, it.toString())  //todo remove it
        })
    }

}
