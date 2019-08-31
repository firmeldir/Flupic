package com.example.flupic.util.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flupic.databinding.FeedItemBinding
import com.example.flupic.domain.FireDish
import com.example.flupic.domain.FlupicDish
import com.google.firebase.firestore.DocumentSnapshot


class FeedRecyclerViewAdapter(private val userUID: String
                              ,private val like: Drawable?, private val unlike: Drawable?
                              ,private val detailListener : (accesId: String, authorId: String ,action: FeedAction) -> Unit)
    : RecyclerView.Adapter<FeedRecyclerViewAdapter.FeedHolder>(){

    var data: List<FlupicDish> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder = FeedHolder.from(userUID, parent, detailListener)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FeedHolder, position: Int) = holder.bind(data[position],like, unlike)


    class FeedHolder(private val userUID: String, val binding: FeedItemBinding,
                     val detailListener : (accesId: String, authorId: String ,action: FeedAction) -> Unit) : RecyclerView.ViewHolder(binding.root){

        private lateinit var  accesId: String
        private lateinit var  authorId: String

        //todo animation like
        private var likeState: Boolean = false

        fun bind(flupicDish: FlupicDish, like: Drawable?, unlike: Drawable?) {
            val dish = flupicDish.dish

            binding.listenCard.setOnClickListener {
                detailListener(accesId, authorId, FeedAction.SELECT)
            }

            binding.likeButton.setOnClickListener {
                detailListener(accesId, authorId, FeedAction.LIKE)
                likeState = !likeState
                if(likeState){
                    binding.likeButton.setImageDrawable(like)
                }else{
                    binding.likeButton.setImageDrawable(unlike)
                }
            }

            if(like != null && unlike != null){
                likeState = if(dish.likes.contains(userUID)){
                    binding.likeButton.setImageDrawable(like)
                    true
                }else{
                    binding.likeButton.setImageDrawable(unlike)
                    false
                }
            }

            binding.dish = dish
            accesId = flupicDish.dishId
            authorId = flupicDish.authorId
            binding.executePendingBindings()
        }

        companion object {
            fun from(userUID:String, parent: ViewGroup, detailListener : (accesId: String, authorId: String ,action: FeedAction) -> Unit): FeedHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeedItemBinding.inflate(layoutInflater, parent, false)
                return FeedHolder(userUID, binding, detailListener)
            }
        }
    }
}



