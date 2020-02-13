package com.example.flupic.scrobbler.service

import android.annotation.TargetApi
import android.app.Service
import android.content.ComponentName
import android.content.Intent

class NotificationListenerService : android.service.notification.NotificationListenerService(){
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = Service.START_STICKY

    @TargetApi(24)
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        requestRebind(ComponentName(applicationContext, NotificationListenerService::class.java))
    }
}