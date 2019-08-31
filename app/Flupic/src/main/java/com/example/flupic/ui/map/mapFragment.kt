package com.example.flupic.ui.map


import android.Manifest
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.flupic.databinding.FragmentMapBinding
import android.widget.Toast
import androidx.core.content.getSystemService
import com.example.flupic.MainActivity
import com.example.flupic.TAG
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult
import android.content.Intent
import android.content.DialogInterface
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.example.flupic.ui.inside.GET_PERMISSION_REQUES
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flupic.R
import com.example.flupic.R.*
import com.example.flupic.di.ViewModelProviderFactory
import com.example.flupic.domain.UserInfoLocation
import com.example.flupic.ui.inside.feedFragmentDirections
import com.example.flupic.util.CustomInfoWindowAdapter
import com.example.flupic.util.getBitmapFromVectorDrawable
import com.example.flupic.viewmodels.EditViewModel
import com.example.flupic.viewmodels.MapViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject


const val ERROR_DIALOG_REQUEST: Int = 20065
const val PERMISSIONS_REQUEST_ENABLE_GPS: Int = 20075
const val GET_PERMISSION_MAP_REQUES: Int = 20085

//todo dagger
class mapFragment : DaggerFragment() , OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    lateinit var binding: FragmentMapBinding

    //Injection of ViewModel
    @Inject
    lateinit var injectFactory: ViewModelProviderFactory

    private val mapViewModel : MapViewModel by lazy {
        ViewModelProviders.of(this, injectFactory).get(
            MapViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener { navBack() }

        initMap()

        return binding.root
    }

    private fun initMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map)

        if (mapFragment is SupportMapFragment)
        mapFragment.getMapAsync(this@mapFragment)
    }


    override fun onMapReady(p0: GoogleMap?) {
        p0?.setOnInfoWindowClickListener(this)
        setupCurLocation(p0)
        setupStartLocation(p0)
        setupMapStyle(p0)
        setupUsersMarkers(p0)
    }

    private fun setupUsersMarkers(googleMap: GoogleMap?){
        mapViewModel.businessLocations.observe(this, Observer {
            if(it != null && googleMap != null){
                val checkString: String? = null
                googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(context!!, checkString))

                for(infoLocation in it){

                    val markerOptions = MarkerOptions().apply {
                        position(LatLng(infoLocation.fireUserLocation!!.geo_point!!.latitude, infoLocation.fireUserLocation.geo_point!!.longitude))
                        icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(context!!, drawable.ic_croissant, 1.5f)))
                    }

                    val marker = googleMap.addMarker(markerOptions)
                    marker.tag = infoLocation
                }
            }
        })
    }

    private fun setupStartLocation(googleMap: GoogleMap?){
        mapViewModel.lastLocation.observe(this, Observer {location ->
            googleMap?.let {map ->
                val geoPoint = location.geo_point
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(geoPoint!!.latitude,geoPoint.longitude), 17f))
            }
        })
    }

    private fun setupMapStyle(googleMap: GoogleMap?){
        try {
            googleMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, raw.map_style))
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "setupMapStyle() : ${e.message}")
        }
    }

    private fun setupCurLocation(googleMap: GoogleMap?){
        if (ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            googleMap?.isMyLocationEnabled = true
        }
    }

    override fun onInfoWindowClick(p0: Marker?) {

        val uid: String? = (p0?.tag as? UserInfoLocation?)?.uid

        uid?.let {
            val action =  mapFragmentDirections.actionMapFragmentToAccountFragment().apply {
                userUID = uid
            }
            findNavController().navigate(action)
        }
    }




    //checkMapServices()
    private fun checkMapServices(){
        if(isServicesEnabled){
            isMapEnabled
        }else{ navBack() }
    }

    private val isServicesEnabled : Boolean
        get() {
            val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)

            when {
                available == ConnectionResult.SUCCESS -> {
                    Log.d(TAG, "isServicesEnabled() : Google Play Services is working")
                    return true
                }
                GoogleApiAvailability.getInstance().isUserResolvableError(available) -> {
                    Log.d(TAG, "isServicesEnabled() : an error occured but we can fix it")
                    val dialog =
                        GoogleApiAvailability.getInstance().getErrorDialog(activity, available, ERROR_DIALOG_REQUEST)
                    dialog.show()
                }
                else -> Toast.makeText(context, "You can't make map requests", Toast.LENGTH_SHORT).show()
            }
            return false
        }

    private val isMapEnabled : Boolean
        get() {
            val manager : LocationManager  = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager

            if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps()
                return false
            }
            return true
        }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(android.Manifest.permission::ACCESS_FINE_LOCATION.get())){
                requestPermissions(arrayOf(android.Manifest.permission::ACCESS_FINE_LOCATION.get()),
                    GET_PERMISSION_MAP_REQUES
                )
            }
            requestPermissions(arrayOf(android.Manifest.permission::ACCESS_FINE_LOCATION.get()),
                GET_PERMISSION_MAP_REQUES
            )
        }
    }

    private fun buildAlertMessageNoGps() {
        AlertDialog.Builder(context)
            .setMessage("This application requires GPS to work properly, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                val enableGpsIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
            }.create().show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GET_PERMISSION_MAP_REQUES -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    navBack()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERMISSIONS_REQUEST_ENABLE_GPS -> {
                val manager : LocationManager  = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager
                if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) { navBack() }
                getLocationPermission()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkMapServices()
    }

    fun navBack() = findNavController().popBackStack()
}
