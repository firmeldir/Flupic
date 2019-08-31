package com.example.flupic.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.flupic.R
import com.example.flupic.TAG
import com.example.flupic.databinding.InfoWindowItemBinding
import com.example.flupic.domain.UserInfoLocation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker



class CustomInfoWindowAdapter(val context: Context, var checkString: String?) : GoogleMap.InfoWindowAdapter{

    lateinit var binding: InfoWindowItemBinding

    override fun getInfoContents(p0: Marker?): View{
        binding = InfoWindowItemBinding.inflate((context as Activity).layoutInflater,null)

        val userInfoLocation: UserInfoLocation? = p0?.tag as? UserInfoLocation?

        p0?.let {
            setPhotoByUriInWindow(userInfoLocation, p0)
            setPublications(binding.publicationsView, userInfoLocation?.publications)
            setUsername(binding.username, userInfoLocation?.username)
        }

        return binding.root
    }

    //todo dagger
    private fun setPhotoByUriInWindow(userInfoLocation: UserInfoLocation?, p0: Marker){

        if(!userInfoLocation?.photo_url.isNullOrBlank()) {
            if(checkString == p0.id){

                Glide.with(binding.profileImage.context)
                    .asBitmap()
                    .load(userInfoLocation?.photo_url)
                    .apply(RequestOptions.placeholderOf(R.drawable.baker).error(R.drawable.baker))
                    .into(binding.profileImage)
            }else{
                checkString = p0.id

                Glide.with(binding.profileImage.context)
                    .asBitmap()
                    .listener(object : RequestListener<Bitmap>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean)
                                : Boolean = false
                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean)
                                : Boolean {
                            p0.showInfoWindow()
                            return true
                        }
                    })
                    .apply(RequestOptions.placeholderOf(R.drawable.baker).error(R.drawable.baker))
                    .load(userInfoLocation?.photo_url)
                    .into(binding.profileImage)
            }
        }
    }


    override fun getInfoWindow(p0: Marker?): View? = null
}