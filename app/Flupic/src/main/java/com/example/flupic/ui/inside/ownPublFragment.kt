package com.example.flupic.ui.inside


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController

import com.example.flupic.R
import com.example.flupic.databinding.FragmentOwnPublBinding
import com.example.flupic.util.adapters.FeedAction
import com.example.flupic.util.adapters.FeedRecyclerViewAdapter
import com.example.flupic.util.adapters.OwnRecyclerViewAdapter
import com.example.flupic.viewmodels.AddViewModel
import com.example.flupic.viewmodels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.feed_item.*
import javax.inject.Inject

class ownPublFragment(private val profileUID:String = "0") : DaggerFragment() {

    lateinit var binding: FragmentOwnPublBinding

    private val profileViewModel : ProfileViewModel by lazy {
        ViewModelProviders.of(parentFragment!!) .get(
            ProfileViewModel::class.java)
    }

    //Firebase
    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var auth: FirebaseAuth


    private val postQuery: Query by lazy {
        if(profileUID == "0"){
            db.collection("users").document(auth.uid.toString())
                .collection("posts").orderBy("date", Query.Direction.DESCENDING).limit(SEARCH_LIMIT.toLong())
        }else{
            db.collection("users").document(profileUID)
                .collection("posts").orderBy("date", Query.Direction.DESCENDING).limit(SEARCH_LIMIT.toLong())
        }
    }

    private val adapter: OwnRecyclerViewAdapter by lazy {
        OwnRecyclerViewAdapter(auth.uid!!,postQuery
            , ContextCompat.getDrawable(context!!, R.drawable.ic_favorite_black_24dp)
            , ContextCompat.getDrawable(context!!,R.drawable.ic_favorite_border)
        ) { d: String, a: String, feedAction: FeedAction ->

            if(feedAction == FeedAction.SELECT){
                val extras = FragmentNavigatorExtras(
                    mainImages to "imageDetail"
                )
                val action =  accountFragmentDirections.actionAccountFragmentToDetailFragment(d, a)
                findNavController().navigate(action,extras)
            }else{
                profileViewModel.like(d, a)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentOwnPublBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    fun setupRecyclerView(){
        binding.ownList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}
