package com.example.flupic.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.example.flupic.R
import com.example.flupic.TAG
import com.example.flupic.databinding.FragmentRegistrationBinding
import com.example.flupic.domain.FireUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_authentication.emailInput
import kotlinx.android.synthetic.main.fragment_authentication.passwordInput
import kotlinx.android.synthetic.main.fragment_registration.*
import javax.inject.Inject

//todo create auth repo

class registrationFragment : DaggerFragment() {

    lateinit var binding: FragmentRegistrationBinding

    @Inject
    lateinit var authInstance: FirebaseAuth

    @Inject
    lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener { navBack() }

        binding.signUpButton.setOnClickListener {
            signUp(emailInput.text.toString(), passwordInput.text.toString(), bioInput.text.toString(), usernameInput.text.toString())
        }

        return binding.root
    }


    fun navBack(){
        findNavController().popBackStack()
    }

    fun signUp(email: String?, password: String?,fullname: String?, username: String? ){

        if(!email.isNullOrBlank() && !password.isNullOrBlank() && !fullname.isNullOrBlank() && !username.isNullOrBlank()){

            db.collection("users").whereEqualTo("username", username).get().addOnSuccessListener {
                if (!it.isEmpty){
                    showToast(getString(R.string.a_error_4))
                    Log.i(TAG, "signUp() : EXIST")
                }else{

                    Log.i(TAG, "signUp() : DON'T EXIST : $username")

                    authInstance.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = task.result?.user

                                firebaseUser?.let {
                                    if (!firebaseUser.isEmailVerified) {
                                        sendVerificationEmailToUser(firebaseUser)
                                    }

                                    val newUser = FireUser(username, fullname)
                                    db.collection("users").document(firebaseUser.uid).set(newUser)
                                }

                            } else {
                                showToast(getString(R.string.a_error_5))
                                Log.i(TAG, "signUp() : ${task.exception.toString()}")
                            }
                        }
                }
            }
        }
    }

    private fun sendVerificationEmailToUser(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { emailTask ->
                if (emailTask.isSuccessful) {
                    Log.i(TAG, "sendVerificationEmailToUser() : SUCCESS")
                }
                else {
                    Log.i(TAG, "sendVerificationEmailToUser() : FAILURE")
                }
            }
    }

    fun showToast(message: CharSequence) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
