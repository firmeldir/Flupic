package com.example.flupic.ui.addTools


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.example.flupic.databinding.FragmentMainInfoAddBinding
import com.example.flupic.ui.inside.CODE_PHOTO_REQUES
import com.example.flupic.ui.inside.GET_PERMISSION_REQUES
import com.example.flupic.viewmodels.AddViewModel


class mainInfoAdd : Fragment() {

    lateinit var binding: FragmentMainInfoAddBinding

    private val addViewModel : AddViewModel by lazy {
        ViewModelProviders.of(parentFragment!!) .get(
            AddViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainInfoAddBinding.inflate(inflater,container, false)

        binding.addPhoto.setOnClickListener {
            loadAndChangePhoto()
        }

        return binding.root
    }

    fun loadAndChangePhoto(){

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(android.Manifest.permission::READ_EXTERNAL_STORAGE.get())){
                requestPermissions(arrayOf(android.Manifest.permission::READ_EXTERNAL_STORAGE.get()),
                    GET_PERMISSION_REQUES
                )
            }
            requestPermissions(arrayOf(android.Manifest.permission::READ_EXTERNAL_STORAGE.get()),
                GET_PERMISSION_REQUES
            )
        }else{
            startGallery()
        }
    }

    fun startGallery(){
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/jpg"
        }

        if (cameraIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(cameraIntent, CODE_PHOTO_REQUES)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CODE_PHOTO_REQUES) {

            data?.let {
                val returnUri: Uri? = data.data

                returnUri?.let {

                    Glide.with(this)
                        .load(returnUri)
                        .into(binding.dishPhoto)

                    addViewModel.photoUrl = returnUri
                }
            }
        }
    }


}
