package com.example.flupic.util

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flupic.TAG
import com.example.flupic.databinding.RecipeItemBinding

class RecipeListAdapter : RecyclerView.Adapter<RecipeListAdapter.RecipeHolder>(){


    var data: Map<String, String> = mapOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder = RecipeHolder.from(parent)

    override fun getItemCount(): Int = data.keys.toList().size

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        holder.bind(data.keys.toList()[position], data.values.toList()[position])
    }


    class RecipeHolder(val binding: RecipeItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(name: String,description: String ) {
            binding.stepName.text = name
            binding.stepDescription.text = description
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecipeHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeItemBinding.inflate(layoutInflater, parent, false)
                return RecipeHolder(binding)
            }
        }
    }
}