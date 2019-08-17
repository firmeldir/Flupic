package com.example.flupic

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import com.example.flupic.ui.inside.addFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

const val TAG = "TAG"

class MainActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth

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
        authListener?.let {
            auth.removeAuthStateListener(authListener)
        }
    }

    fun setupFirebase(){
        auth = FirebaseAuth.getInstance()

        authListener = FirebaseAuth.AuthStateListener{

            val navController = findNavController(R.id.mainNavHostFragment)

            if(it.currentUser == null  || !it.currentUser!!.isEmailVerified){
                navController.navigate(R.id.authenticationFragment)

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
