package com.example.flupic.ui.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.flupic.BaseApplication
import com.example.flupic.R
import com.example.flupic.databinding.ActivityMainBinding
import com.example.flupic.result.EventObserver
import com.example.flupic.util.viewModelProvider
import javax.inject.Inject

class MainActivity : AppCompatActivity(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewmodel: MainViewModel

    private lateinit var binding : ActivityMainBinding

    private val navController: NavController
        get() = findNavController(R.id.mainNavHostFragment)

    override fun onCreate(savedInstanceState: Bundle?)  {
        inject()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewmodel = viewModelProvider(viewModelFactory)

        setupNavGraph()
        setupAuthenticationControl()
    }

    private fun setupNavGraph(){
        val navGraph = navController.navInflater.inflate(R.navigation.navigation)

        val destination = when(intent.getSerializableExtra(LAUNCH_DESTINATION)){
            LaunchDestination.APPLICATION -> R.id.profileFragment
            LaunchDestination.FULLY_REGISTRATION -> R.id.infoSupplyFragment
            else -> R.id.authFragment
        }

        navGraph.startDestination = destination
        navController.graph = navGraph
    }

    private fun setupAuthenticationControl(){
        viewmodel.startAuthenticationAction.observe(this, EventObserver{
            navController.navigate(R.id.authFragment)
        })
    }

    private fun inject() = with((application as BaseApplication).appComponent){
        this.inject(this@MainActivity)
    }

    companion object {
        private const val LAUNCH_DESTINATION ="launch-destination"

        fun Activity.startMainActivity(destination: LaunchDestination) {
            this.startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra(LAUNCH_DESTINATION, destination)
            })
        }
    }
}
