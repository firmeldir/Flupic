package com.example.flupic.util

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.flupic.result.ActivityRequestHandler

object PickPhotoRequestHandler : ActivityRequestHandler{
    private const val CODE_GALLERY_RESULT_REQUEST = 1015

    private val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply { type = "image/jpg" }

    override fun request(activity: Activity) {
        if (cameraIntent.resolveActivity(activity.packageManager) != null) {
            ActivityCompat.startActivityForResult(
                activity,
                cameraIntent,
                CODE_GALLERY_RESULT_REQUEST,
                null
            )
        }
    }

    override fun request(fragment: Fragment) {
        if (cameraIntent.resolveActivity(fragment.activity!!.packageManager) != null) {
            fragment.startActivityForResult(
                cameraIntent,
                CODE_GALLERY_RESULT_REQUEST,
                null
            )
        }
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) : Intent? =
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CODE_GALLERY_RESULT_REQUEST -> { data }
                else ->     null
            }
        }else    null

}