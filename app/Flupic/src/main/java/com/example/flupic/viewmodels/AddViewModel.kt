package com.example.flupic.viewmodels

import android.net.Uri
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flupic.TAG
import com.example.flupic.domain.FireDish
import com.example.flupic.domain.FireRecipe
import com.example.flupic.domain.FireUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class AddViewModel : ViewModel(){

    //Coroutines
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    //Firebase
    private val auth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("users").document(auth.uid.toString()).collection("posts")

    private val storage = FirebaseStorage.getInstance()
    private var storageRef = storage.reference

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
            _postDish(fireDish, fireRecipe, _isDone, ref, photoUrl)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

    //todo do in repo
    suspend fun _postDish(fireDish: FireDish, fireRecipe: FireRecipe, _isDone: MutableLiveData<Boolean>, ref: CollectionReference, uri: Uri?){

        try{
            withContext(Dispatchers.IO){

                //todo transaction
                //todo change to repo call
                val userRef = db.collection("users").document(auth.uid.toString())
                val userSnapshot = userRef.get().await()
                val user = userSnapshot.toObject(FireUser::class.java)

                user?.let {
                    fireDish.author = user.username
                }

                val postDoc = ref.add(fireDish).await()
                ref.document(postDoc.id).collection("recipes").document("recipe").set(fireRecipe).await()
                saveRecipePhoto(uri,postDoc.id, ref.document(postDoc.id))

                userRef.update("publications", FieldValue.increment(1)).await()
            }
            _isDone.value = true

        }catch (e: Exception){
            Log.i(TAG, e.message)
            _isDone.value = true
        }

    }

    //todo do in repo
    suspend fun saveRecipePhoto(uri: Uri?, postId: String, postRef: DocumentReference){
        uri?.let {

            val imageRef = storageRef.child("recipes/$postId/1.jpg")

            try {
                imageRef.delete().await()
            }catch (e: Exception){
                Log.i(TAG, e.message)
            }

            var downloadURL : Uri? = null

            try {
                val task = imageRef.putFile(uri).continueWithTask{
                    if (!it.isSuccessful){
                        throw it.exception!!
                    }
                    return@continueWithTask imageRef.downloadUrl
                }

                downloadURL = task.await()

            }catch (e: Exception){
                Log.i(TAG, e.message)
            }

            try {

                postRef.update(mapOf(
                    "photo_url" to downloadURL.toString()
                )).await()

            }catch (e: Exception){
                Log.i(TAG, e.message)
            }
        }
}

}

