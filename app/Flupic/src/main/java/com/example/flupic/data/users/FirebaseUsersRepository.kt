package com.example.flupic.data.users

import android.util.Log
import com.example.flupic.data.AppDatabase
import com.example.flupic.data.users.local.toUserEntity
import com.example.flupic.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUsersRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val database: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRepository {


    override suspend fun getUser(forceUpdate: Boolean) : User  = withContext(ioDispatcher){
        val uid = auth.uid
        if(uid.isNullOrEmpty()) throw IllegalStateException("Non authenticated")

        val ref = firestore.collection(USERS_COLLECTION).document(uid)

        if(forceUpdate){
            val userSnapshot = ref.get(Source.SERVER).await()
            val user = parseUser(userSnapshot)
            database.userDao().insertUser(user.toUserEntity())
            user
        }else{

            return@withContext try {
                val userSnapshot = ref.get(Source.DEFAULT).await()
                parseUser(userSnapshot)
            }catch (e: Exception){
                Log.e(TAG, e.message.toString())
                database.userDao().getUser(uid).toUser()
            }
        }
    }

    override suspend fun getPeopleByUid(uid: String, forceUpdate: Boolean) : User  = withContext(ioDispatcher){
        val ref = firestore.collection(USERS_COLLECTION).document(uid)
        val userSnapshot = ref.get( if(forceUpdate)Source.SERVER else Source.DEFAULT ).await()
        parseUser(userSnapshot)
    }

    private fun parseUser(snapshot: DocumentSnapshot) : User =
        User(
            snapshot.id,
            snapshot[USERNAME] as? String ?: "",
            snapshot[NAME] as? String ?: "",
            snapshot[BIO] as? String ?: "",
            snapshot[PHOTO_URL] as? String ?: "",
            snapshot[PHONE_NUMBER] as? String ?: ""
        )


    override fun getUserContentQuery() : Query{
        val uid = auth.uid
        if(uid.isNullOrEmpty()) throw IllegalStateException("Non authenticated")

        return getPeopleContentQueryByUid(uid)
    }

    override fun getPeopleContentQueryByUid(uid: String) : Query =
        firestore.collection(USERS_COLLECTION).document(uid).collection(POSTS_COLLECTION).orderBy(DATE)

    companion object{
        private const val TAG = "TAG UsersRepository"


        private const val USERS_COLLECTION = "users"

        private const val USERNAME = "username"
        private const val NAME = "name"
        private const val BIO = "bio"
        private const val PHOTO_URL = "photoUrl"
        private const val PHONE_NUMBER = "phoneNumber"


        private const val POSTS_COLLECTION = "posts"

        private const val DATE = "date"
    }
}