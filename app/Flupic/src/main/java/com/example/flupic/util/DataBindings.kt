package com.example.flupic.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.flupic.domain.FireUser
import com.google.firebase.Timestamp

@BindingAdapter("setFollowing")
fun setFollowing(button: com.google.android.material.button.MaterialButton, fireUser: FireUser?)
{
    button.text = "${fireUser?.following?.size}\nFollowing"
}

@BindingAdapter("setFollowers")
fun setFollowers(button: com.google.android.material.button.MaterialButton, fireUser: FireUser?)
{
    button.text = "${fireUser?.followers}\nFollowers"
}

@BindingAdapter("setPublications")
fun setPublications(textView: TextView, fireUser: FireUser?)
{
    textView.text = "${fireUser?.publications} publications"
}

@BindingAdapter("setNum")
fun setNum(textView: TextView, fireUser: FireUser?)
{
    textView.text = fireUser?.phone_num
}

@BindingAdapter("setFullname")
fun setFullname(textView: TextView, fireUser: FireUser?)
{
    textView.text = fireUser?.fullname
}

@BindingAdapter("setBio")
fun setBio(textView: TextView, fireUser: FireUser?)
{
    if(fireUser?.bio.isNullOrBlank()){
        textView.isGone = true
    }else{
        textView.isGone = false
        textView.text = fireUser?.bio
    }
}

@BindingAdapter("setCategory")
fun setCategory(textView: TextView, fireUser: FireUser?)
{
    if(fireUser?.category.isNullOrBlank()){
        textView.isGone = true
    }else{
        textView.isGone = false
        textView.text = fireUser?.category
    }
}

@BindingAdapter("setProfilePhoto")
fun setProfilePhoto(imageView: ImageView, fireUser: FireUser?)
{
    fireUser?.let {
        if(!fireUser.photo_url.isBlank()){
            Glide.with(imageView.context)
                .load(fireUser.photo_url)
                .into(imageView)
        }
    }
}


@BindingAdapter("setPhotoByUri")
fun setPhotoByUri(imageView: ImageView, photo_url: String?)
{
    if(!photo_url.isNullOrBlank()) {
        Glide.with(imageView.context)
            .load(photo_url)
            .into(imageView)
    }
}

@BindingAdapter("setDateFromTimestamp")
fun setDateFromTimestamp(textView: TextView, timestamp: Timestamp)
{
    textView.text = "${timestamp.toDate().toLocaleString()}"
}


