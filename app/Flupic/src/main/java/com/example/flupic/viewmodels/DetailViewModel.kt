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
        get(){
            Log.i(TAG, "H ${_recipe.value}")
            return _recipe
        }

    var accessId : String = ""
        private set


    fun loadInfoById(accessId :  String){
        this.accessId = accessId
        getDetailData()
    }

    fun getDetailData(){
        coroutineScope.launch {
            insideRepository.getDish(_dish, accessId)
            insideRepository.getRecipe(_recipe, accessId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}