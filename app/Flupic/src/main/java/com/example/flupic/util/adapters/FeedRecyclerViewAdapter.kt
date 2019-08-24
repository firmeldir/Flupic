package com.example.flupic.util.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flupic.databinding.FeedItemBinding
import com.example.flupic.domain.FireDish
import com.google.firebase.firestore.DocumentSnapshot

import com.google.firebase.firestore.Query

enum class FeedAction{
    LIKE, SELECT
}

class FeedRecyclerViewAdapter(userUID: String
                              ,query: Query
                              ,private val like: Drawable?, private val unlike: Drawable?
                              ,private val detailListener : (id: String, action: FeedAction) -> Unit)
    : RecyclerFirestoreAdapter<FeedRecyclerViewAdapter.FeedHolder>(query){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder = FeedHolder.from(parent, detailListener)

    override fun onBindViewHolder(holder: FeedHolder, position: Int) = holder.bind(snapshots[position], like, unlike)

    companion object ID{
        internal var userUID = ""
    }

    init {
        ID.userUID = userUID
    }

    class FeedHolder(val binding: FeedItemBinding, val detailListener : (id: String, action: FeedAction) -> Unit) : RecyclerView.ViewHolder(binding.root){

        private lateinit var  accesId: String

        fun bind(snapshot: DocumentSnapshot, like: Drawable?, unlike: Drawable?) {
            val fireDish = snapshot.toObject(FireDish::class.java)

            binding.listenCard.setOnClickListener {
                detailListener(accesId, FeedAction.SELECT)
            }

            binding.likeButton.setOnClickListener {
                detailListener(accesId, FeedAction.LIKE)
            }

            if(fireDish != null && like != null && unlike != null){
                if(fireDish.likes.contains(ID.userUID)){
                    binding.likeButton.setImageDrawable(like)
                }else{
                    binding.likeButton.setImageDrawable(unlike)
                }
            }

            binding.dish = fireDish
            accesId = snapshot.id
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, detailListener : (id: String, action: FeedAction) -> Unit): FeedHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeedItemBinding.inflate(layoutInflater, parent, false)
                return FeedHolder(binding, detailListener)
            }
        }
    }
}