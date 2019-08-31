package com.example.flupic.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flupic.TAG
import com.example.flupic.domain.FireDish
import com.example.flupic.domain.FireRecipe
import com.example.flupic.repository.InsideRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(val insideRepository: InsideRepository) : ViewModel(){

    //Coroutines
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    //Dish
    private val _dish: MutableLiveData<FireDish> = MutableLiveData()
    val dish: LiveData<FireDish>
        get() = _dish

    //Recipe
    private val _recipe: MutableLiveData<FireRecipe> = MutableLiveData()
    val recipe: LiveData<FireRecipe>
        get() = _recipe


    fun loadInfoById(accessId :  String, authorId: String){
        getDetailData(accessId, authorId)
    }

    private fun getDetailData(accessId :  String, authorId: String){
        coroutineScope.launch {
            if(authorId == "default"){
                insideRepository.getDish(_dish, accessId)
                insideRepository.getRecipe(_recipe, accessId)
            }else{
                insideRepository.getDish(_dish, accessId, authorId)
                insideRepository.getRecipe(_recipe, accessId, authorId)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}