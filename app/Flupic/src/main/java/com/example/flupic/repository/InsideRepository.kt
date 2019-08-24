package com.example.flupic.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.flupic.TAG
import com.example.flupic.domain.FireDish
import com.example.flupic.domain.FireRecipe
import com.example.flupic.domain.FireUser
import com.example.flupic.util.adapters.FeedRecyclerViewAdapter.ID.userUID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsideRepository @Inject constructor(private val auth : FirebaseAuth,
                                           private val db : FirebaseFirestore,
                                           private val storage : FirebaseStorage){


    suspend fun getUser(curUser: MutableLiveData<FireUser>){

        val ref = db.collection("users").document(auth.uid.toString())
        var userSnapshot: DocumentSnapshot? = null

        try {
            withContext(Dispatchers.IO){ userSnapshot = ref.get().await() }

            userSnapshot?.let {
                curUser.value = userSnapshot?.toObject(FireUser::class.java)
            }

        }catch (e: Exception){
            Log.e(TAG,"getUser() : ${e.message}")
        }
    }

    suspend fun applyChanges(fullname: String, bio: String, phone_num: String, category: String, uri: Uri?, isDone: MutableLiveData<Boolean>){

        val userRef = db.collection("users").document(auth.uid.toString())

        try {
            withContext(Dispatchers.IO){
                updateFields(fullname, bio, phone_num, category, userRef)
                saveProfilePhoto(uri, userRef)
            }

            isDone.value = true
        }catch (e: Exception){
            Log.e(TAG,"applyChanges() : ${e.message}")
            isDone.value = true
        }
    }

    private suspend fun updateFields(fullname: String, bio: String, phone_num: String, category: String,userRef: DocumentReference){
        userRef.update(mapOf(
            "fullname" to fullname,
            "phone_num" to phone_num,
            "bio" to bio,
            "category" to category
        )).await()
    }

    suspend fun postDish(fireDish: FireDish, fireRecipe: FireRecipe, uri: Uri?, isDone: MutableLiveData<Boolean>){

        val postsRef = db.collection("users").document(auth.uid.toString()).collection("posts")
        val userRef = db.collection("users").document(auth.uid.toString())

        try{
            withContext(Dispatchers.IO){

                //todo transaction

                //Get dish author
                val user = userRef.get().await().toObject(FireUser::class.java)
                user?.let {
                    fireDish.author = user.username
                }

                //Post dish
                val postDoc = postsRef.add(fireDish).await()

                //Post dish recipe
                postsRef.document(postDoc.id).collection("recipes").document("recipe").set(fireRecipe).await()

                //Post recipe photo
                saveRecipePhoto(uri, postDoc.id, postsRef.document(postDoc.id))

                //New post
                userRef.update("publications", FieldValue.increment(1)).await()
            }

            isDone.value = true
        }catch (e: Exception){
            Log.e(TAG,"postDish() : ${e.message}")
            isDone.value = true
        }
    }

    //todo unite 2 methods in one flexible
    suspend fun saveRecipePhoto(uri: Uri?, postId: String, postRef: DocumentReference){
        uri?.let {

            val imageRef = storage.reference.child("recipes/$postId/1.jpg")
            var downloadURL : Uri? = null

            //Delete old photo
            try {
                imageRef.delete().await()
            }catch(e: Exception){
                Log.i(TAG,"saveProfilePhoto() : ${e.message}")
            }

            downloadURL = putPhotoFile(imageRef, uri)

            try {
                postRef.update(mapOf(  "photo_url" to downloadURL.toString()  )).await()
            }catch (e: java.lang.Exception){
                Log.e(TAG,"saveProfilePhoto() : ${e.message}")
            }
        }
    }

    private suspend fun putPhotoFile(imageRef : StorageReference, uri: Uri) : Uri?{
        try {
            val task = imageRef.putFile(uri).continueWithTask{
                if (!it.isSuccessful){
                    throw it.exception!!
                }
                return@continueWithTask imageRef.downloadUrl
            }

            return task.await()
        }catch (e: java.lang.Exception){
            Log.e(TAG,"putPhotoFile() : ${e.message}")
            return null
        }
    }

    private suspend fun saveProfilePhoto(uri: Uri?, userRef: DocumentReference){
        uri?.let {

            val imageRef = storage.reference.child("users/${auth.uid}/avatar.jpg")
            var downloadURL : Uri? = null

            //Delete old photo
            try {
                imageRef.delete().await()
            }catch(e: Exception){
                Log.i(TAG,"saveProfilePhoto() : ${e.message}")
            }

            downloadURL = putPhotoFile(imageRef, uri)

            //Update photo url
            try {
                userRef.update(mapOf(  "photo_url" to downloadURL.toString()  )).await()

            }catch (e: Exception){
                Log.e(TAG,"saveProfilePhoto() : ${e.message}")
            }
        }
    }

    suspend fun getDish(fireDish: MutableLiveData<FireDish>, accessId: String){

        val ref = db.collection("users").document(auth.uid.toString()).collection("posts").document(accessId)
        var dishSnapshot: DocumentSnapshot? = null

        try {
            withContext(Dispatchers.IO){ dishSnapshot = ref.get().await() }

            dishSnapshot?.let {
                fireDish.value = dishSnapshot?.toObject(FireDish::class.java)
            }

        }catch (e: Exception){
            Log.e(TAG,"getDish() : ${e.message}")
        }
    }


    suspend fun getRecipe(fireRecipe: MutableLiveData<FireRecipe>, accessId: String){

        val ref = db.collection("users").document(auth.uid.toString()).collection("posts").document(accessId)
            .collection("recipes").document("recipe")
        var recipeSnapshot: DocumentSnapshot? = null

        try {
            withContext(Dispatchers.IO){ recipeSnapshot = ref.get().await() }

            recipeSnapshot?.let {
                fireRecipe.value = recipeSnapshot?.toObject(FireRecipe::class.java)
            }

        }catch (e: Exception){
            Log.e(TAG,"getDish() : ${e.message}")
        }
    }

    suspend fun like(accessId: String){

        val ref = db.collection("users").document(auth.uid.toString()).collection("posts").document(accessId)

        try {
            val documentSnapshot = ref.get().await()

            val list = documentSnapshot?.toObject(FireDish::class.java)?.likes

            list?.let {

                if(list.contains(auth.uid.toString())){
                    ref.update("likes", FieldValue.arrayRemove(auth.uid.toString())).await()
                }else{
                    ref.update("likes", FieldValue.arrayUnion(auth.uid.toString())).await()
                }
            }
        }catch (e: Exception){
            Log.e(TAG,"like() : ${e.message}")
        }
    }
}