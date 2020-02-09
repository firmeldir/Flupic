package com.example.flupic.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.flupic.BaseApplication
import com.example.flupic.R
import com.example.flupic.result.EventObserver
import com.example.flupic.ui.activity.MainActivity.Companion.startMainActivity
import com.example.flupic.util.viewModelProvider
import javax.inject.Inject

class LaunchActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        val viewModel : LaunchActivityViewModel = viewModelProvider(viewModelFactory)

        viewModel.launchDestination.observe(this, EventObserver{destination ->
            startMainActivity(destination)
            finish()
        })
    }

    private fun inject() = with((application as BaseApplication).appComponent){
        this.inject(this@LaunchActivity)
    }
}
