package com.example.flupic.util.adapters

import android.graphics.drawable.Drawable
import android.os.Build.ID
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flupic.TAG
import com.example.flupic.databinding.FeedItemBinding
import com.example.flupic.domain.FireDish
import com.google.firebase.firestore.DocumentSnapshot

import com.google.firebase.firestore.Query

enum class FeedAction{
    LIKE, SELECT
}

class OwnRecyclerViewAdapter(
    private val userUID: String
    , query: Query
    , private val like: Drawable?, private val unlike: Drawable?
    , private val detailListener : (accesId: String, authorId: String ,action: FeedAction) -> Unit)
    : RecyclerFirestoreAdapter<OwnRecyclerViewAdapter.FeedHolder>(query){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder = FeedHolder.from(userUID,parent,detailListener)

    override fun onBindViewHolder(holder: FeedHolder, position: Int) = holder.bind(snapshots[position], like, unlike)


    class FeedHolder(
        private val userUID: String,
        val binding: FeedItemBinding
        , val detailListener : (accesId: String, authorId: String ,action: FeedAction) -> Unit) : RecyclerView.ViewHolder(binding.root){

        private lateinit var  accesId: String
        private lateinit var  authorId: String

        fun bind(snapshot: DocumentSnapshot, like: Drawable?, unlike: Drawable?) {

            val fireDish = snapshot.toObject(FireDish::class.java)

            binding.listenCard.setOnClickListener {
                detailListener(accesId, authorId, FeedAction.SELECT)
            }

            binding.likeButton.setOnClickListener {
                detailListener(accesId,  authorId,FeedAction.LIKE)
            }

            if(fireDish != null && like != null && unlike != null){
                if(fireDish.likes.contains(userUID)){
                    binding.likeButton.setImageDrawable(like)
                }else{
                    binding.likeButton.setImageDrawable(unlike)
                }
            }

            binding.dish = fireDish
            accesId = snapshot.id
            authorId = snapshot.reference.parent.parent!!.id
            binding.executePendingBindings()
        }

        companion object {
            fun from(userUID: String, parent: ViewGroup, detailListener : (accesId: String, authorId: String ,action: FeedAction) -> Unit): FeedHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeedItemBinding.inflate(layoutInflater, parent, false)
                return FeedHolder(userUID, binding, detailListener)
            }
        }
    }
}