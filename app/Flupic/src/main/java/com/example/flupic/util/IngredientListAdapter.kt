package com.example.flupic.util

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flupic.TAG
import com.example.flupic.databinding.IngredientItemBinding

class IngredientListAdapter : RecyclerView.Adapter<IngredientListAdapter.IngredientHolder>(){

    var data: List<String> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientHolder = IngredientHolder.from(parent)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: IngredientHolder, position: Int) = holder.bind(data[position])


    class IngredientHolder(val binding: IngredientItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(string: String) {
            binding.indredient = string
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): IngredientHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientItemBinding.inflate(layoutInflater, parent, false)
                return IngredientHolder(binding)
            }
        }
    }
}
