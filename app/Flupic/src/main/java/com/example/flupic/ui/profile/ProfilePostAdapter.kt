package com.example.flupic.ui.profile

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.example.flupic.model.Post
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions

class ProfilePostAdapter private constructor(options: FirestorePagingOptions<Post>)
    : FirestorePagingAdapter<Post, ProfilePostAdapter.ProfilePostItem>(options) {

    companion object{

        private const val PAGE_SIZE = 14
        private const val PREFETCH_DISTANCE = 7

        fun create(owner: LifecycleOwner){

            val baseQuery = firestore.collection("users").document(auth.uid!!).collection("posts")

            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setPageSize(PAGE_SIZE)
                .build()

            val options = FirestorePagingOptions.Builder<Post>()
                .setLifecycleOwner(owner)
                .setQuery(baseQuery , config, Post::class.java)
                .build()
        }
    }

    init {


    }

    class ProfilePostItem(f: View) : RecyclerView.ViewHolder(f){

    }

}