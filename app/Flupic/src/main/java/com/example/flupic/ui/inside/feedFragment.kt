package com.example.flupic.ui.inside


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.flupic.R
import com.example.flupic.databinding.FragmentFeedBinding
import com.example.flupic.util.adapters.FeedRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

const  val SEARCH_LIMIT = 5

class feedFragment : Fragment() {

    lateinit var binding: FragmentFeedBinding

    //Firebase
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    private val postQuery: Query = db.collection("users").document(auth.uid.toString())
        .collection("posts").orderBy("date").limit(SEARCH_LIMIT.toLong())
    private val adapter: FeedRecyclerViewAdapter = FeedRecyclerViewAdapter(postQuery)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFeedBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    fun setupRecyclerView(){
        binding.recyclerView.adapter = adapter
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
