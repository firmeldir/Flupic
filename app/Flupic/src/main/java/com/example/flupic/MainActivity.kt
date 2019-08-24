package com.example.flupic


import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

const val TAG = "TAG"

class MainActivity : DaggerAppCompatActivity(){

    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var authListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFirebase()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        authListener.let {
            auth.removeAuthStateListener(authListener)
        }
    }

    fun setupFirebase(){
        authListener = FirebaseAuth.AuthStateListener{

            val navController = findNavController(R.id.mainNavHostFragment)

            if(it.currentUser == null  || !it.currentUser!!.isEmailVerified){

                if(navController.currentDestination!!.id == R.id.insideFragment){
                    navController.navigate(R.id.action_insideFragment_to_authenticationFragment2)
                }else{
                    navController.navigate(R.id.authenticationFragment)
                }

                it.currentUser?.let {
                    if(!it.isEmailVerified){
                        Toast.makeText(this, getString(R.string.a_error_3).toString(), Toast.LENGTH_SHORT).show()
                        auth.signOut()
                    }
                }
            }else{
                if(navController.currentDestination!!.id == R.id.authenticationFragment){
                    navController.navigate(R.id.action_authenticationFragment_to_insideFragment2)
                }else{
                    navController.navigate(R.id.insideFragment)
                }
            }
        }
    }
}
