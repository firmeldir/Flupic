package com.example.flupic.ui.inside


import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flupic.R
import com.example.flupic.TAG
import com.example.flupic.databinding.FragmentInsideBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class insideFragment : DaggerFragment() {

    lateinit var binding: FragmentInsideBinding

    //todo create auth repo
    @Inject
    lateinit var authInstance: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInsideBinding.inflate(inflater, container, false)

        setupUI()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setupUI(){

        //Bottom Nav
        if(childFragmentManager.findFragmentById(R.id.bottomNavHostFragment) is NavHostFragment){
            val host: NavHostFragment = childFragmentManager.findFragmentById(R.id.bottomNavHostFragment) as NavHostFragment

            binding.bottomNavigationView.setupWithNavController(host.navController)

            host.navController.addOnDestinationChangedListener { controller, destination, arguments ->
                if(destination.id == R.id.addFragment || destination.id == R.id.editFragment  || destination.id == R.id.detailFragment){
                    binding.downMotionLayout.transitionToEnd()
                }else{
                    binding.downMotionLayout.transitionToStart()
                }
            }
        }

        //Toolbar
        val sActivity = activity
        if(sActivity is AppCompatActivity){
            sActivity.setSupportActionBar(binding.mainToolbar)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.signOut -> {authInstance.signOut()
                true}
            R.id.editProfile -> {

                if(childFragmentManager.findFragmentById(R.id.bottomNavHostFragment) is NavHostFragment){
                    val host: NavHostFragment = childFragmentManager.findFragmentById(R.id.bottomNavHostFragment) as NavHostFragment
                    host.navController.navigate(R.id.editFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
