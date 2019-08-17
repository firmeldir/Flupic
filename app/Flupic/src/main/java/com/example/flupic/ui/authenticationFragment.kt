package com.example.flupic.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.flupic.MainActivity

import com.example.flupic.R
import com.example.flupic.databinding.FragmentAuthenticationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_authentication.*


class authenticationFragment : Fragment() {

    lateinit var binding: FragmentAuthenticationBinding

    lateinit var authInstance: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)

        authInstance = FirebaseAuth.getInstance()

        binding.signUpButton.setOnClickListener{signUp()}

        binding.logInButton.setOnClickListener { logIn(binding.emailInput.text.toString(),binding.passwordInput.text.toString() ) }

        return binding.root
    }

    fun logIn(email: String?, password: String?){

        if(!email.isNullOrBlank() && !password.isNullOrBlank()){

            binding.logInButton.isEnabled = false
            binding.signUpButton.isEnabled = false

            authInstance.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding.logInButton.isEnabled = true
                        binding.signUpButton.isEnabled = true

                    } else {
                        showToast(getString(R.string.a_error_2))
                        binding.logInButton.isEnabled = true
                        binding.signUpButton.isEnabled = true
                    }
                }
        }
    }

    fun signUp(){
        findNavController().navigate(R.id.action_authenticationFragment_to_registrationFragment2)
    }

    fun showToast(message: CharSequence) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
