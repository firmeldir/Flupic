package com.example.flupic.result

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

open class ActivityRequestEvent<T : ActivityRequestHandler>(content: T) {

    private val event = Event(content)

    fun getContentIfNotHandled(): T? = event.getContentIfNotHandled()
}

interface ActivityRequestHandler{

    fun request(activity: Activity)

    fun request(fragment: Fragment)

    fun onResult(requestCode: Int, resultCode: Int, data: Intent?) : Intent?
}

class ActivityRequestEventObserver<T: ActivityRequestHandler> : Observer<ActivityRequestEvent<T>> {

    private val activity: Activity?
    private val fragment: Fragment?

    constructor(fragment: Fragment){
        this.fragment = fragment
        activity = null
    }

    constructor(activity: Activity){
        this.activity = activity
        fragment = null
    }

    override fun onChanged(event: ActivityRequestEvent<T>?) {
        activity?.let { event?.getContentIfNotHandled()?.request(activity) }

        fragment?.let { event?.getContentIfNotHandled()?.request(fragment) }
    }
}