package com.example.flupic.util.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flupic.databinding.FeedItemBinding
import com.example.flupic.domain.FireDish
import com.google.firebase.firestore.DocumentSnapshot

import com.google.firebase.firestore.Query

class FeedRecyclerViewAdapter(query: Query) : RecyclerFirestoreAdapter<FeedRecyclerViewAdapter.FeedHolder>(query){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder = FeedHolder.from(parent)

    override fun onBindViewHolder(holder: FeedHolder, position: Int) = holder.bind(snapshot = snapshots[position])


    class FeedHolder(val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(snapshot: DocumentSnapshot) {
            val fireDish = snapshot.toObject(FireDish::class.java)
            binding.dish = fireDish
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FeedHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeedItemBinding.inflate(layoutInflater, parent, false)
                return FeedHolder(binding)
            }
        }
    }

}