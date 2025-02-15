package com.example.flupic.viewmodels

import android.net.Uri
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.example.flupic.TAG
import com.example.flupic.domain.FireDish
import com.example.flupic.domain.FireRecipe
import com.example.flupic.domain.FireUser
import com.example.flupic.repository.InsideRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AddViewModel @Inject constructor(val insideRepository : InsideRepository) : ViewModel(){

    //Coroutines
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    //Lists to fill
    private val _recipe: MutableLiveData<Map<String,String>> = MutableLiveData()
    val recipe: LiveData<Map<String,String>>
    get() = _recipe

    private val _ingredient: MutableLiveData<List<String>> = MutableLiveData()
    val ingredient: LiveData<List<String>>
    get() = _ingredient

    //Exit when Done
    private val _isDone: MutableLiveData<Boolean> = MutableLiveData()
    val isDone: LiveData<Boolean>
    get() = _isDone

    //Photo to save
    lateinit var photoUrl: Uri

    init {
        _isDone.value = false
        _recipe.value = mapOf()
        _ingredient.value = listOf()
    }

    fun addIngredient(ingName: String, ingAmount: String){
        val list: MutableList<String> = _ingredient.value!!.toMutableList() ; list.add("$ingName  ($ingAmount)")
        _ingredient.value = list
    }

    fun addRecipeItem(recipeItemName: String,recipeItemDescription: String){
        val map: MutableMap<String, String> = _recipe.value!!.toMutableMap(); map[recipeItemName] = recipeItemDescription
        _recipe.value = map
    }

    fun postDish(fireDish: FireDish, fireRecipe: FireRecipe){

        fireRecipe.apply {
            ingredients = _ingredient.value!!
            recipe = _recipe.value!!
        }

        coroutineScope.launch {
            insideRepository.postDish(fireDish, fireRecipe, photoUrl, _isDone)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}

