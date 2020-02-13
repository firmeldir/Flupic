package com.example.flupic.ui.test


import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.flupic.BaseApplication
import com.example.flupic.databinding.FragmentTestBinding
import com.example.flupic.scrobbler.service.MusicSharingService
import com.example.flupic.scrobbler.service.MusicSharingServiceConnecter
import javax.inject.Inject


class TestFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentTestBinding


    // * //TODO : DELETE

    private var bound = false

    @Inject
    lateinit var connecter: MusicSharingServiceConnecter

    private var service: MusicSharingService? = null

    private val connection: ServiceConnection = object : ServiceConnection{

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as? MusicSharingService.LocalBinder
            service = binder?.service
            bound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            service = null
            bound = false
        }
    }

    override fun onStart() {
        super.onStart()

        if(!isNotificationServiceEnabled)
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))

        requestPermission()

        activity!!.bindService( Intent(context!!, MusicSharingService::class.java), connection, Context.BIND_AUTO_CREATE )
    }

    override fun onStop() {
        if(bound){
            activity!!.unbindService(connection)
            bound = false
        }

        super.onStop()
    }

    private val isNotificationServiceEnabled: Boolean
        get(){
            val enabledNotificationListeners = Settings.Secure.getString(context?.contentResolver, "enabled_notification_listeners")
            val packageName = context?.packageName ?: return false
            return !(enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName))
        }

    private fun requestPermission(){
        val permissionAccessCoarseLocationApproved = ActivityCompat
            .checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (permissionAccessCoarseLocationApproved) {

        } else {
            // App doesn't have access to the device's location at all. Make full request
            // for permission.
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                11333
            )
        }

        val backgroundLocationPermissionApproved = ActivityCompat
            .checkSelfPermission(context!!, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (backgroundLocationPermissionApproved) {
            // App can access location both in the foreground and in the background.
            // Start your service that doesn't have a foreground service type
            // defined.
        } else {
            // App can only access location in the foreground. Display a dialog
            // warning the user that your app must have all-the-time access to
            // location in order to function properly. Then, request background
            // location.
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), 11222)
        }
    }

    // *

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()
        binding = FragmentTestBinding.inflate(inflater, container, false)

        connecter.isScrobbling.observe(this, Observer {
            binding.launched.text = it.toString()
        })

        binding.launchButton.setOnClickListener {
            service?.startSharing()
        }

        binding.stopButton.setOnClickListener {
            Log.i("VLAD", "ONCLICK ${service.toString()}")
            service?.stopSharing()
        }

        return binding.root
    }


    private fun inject() = with((activity!!.application as BaseApplication).appComponent){
        this.inject(this@TestFragment)
    }
}
