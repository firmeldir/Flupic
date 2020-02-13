package com.example.flupic.scrobbler.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.flupic.BaseApplication
import com.example.flupic.scrobbler.MusicSharingController
import com.example.flupic.scrobbler.util.NotificationUtil
import com.example.flupic.scrobbler.util.NotificationUtil.EXTRA_STARTED_FROM_NOTIFICATION
import com.example.flupic.scrobbler.util.checkLocationPermissionAccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import android.os.Build
import androidx.core.content.ContextCompat


/*
*
*  Make changes to class if screen orientation is enabled .
*
 */

class MusicSharingService : Service(){

    @Inject
    lateinit var connecter: MusicSharingServiceConnecter


    @Inject
    lateinit var musicSharingController: MusicSharingController


    private val onActiveSessionsChangedListener = MediaSessionManager.OnActiveSessionsChangedListener{ updateSessionsControl(it) }



    fun startSharing(){
        if(checkLocationPermissionAccess(connecter.error)){
            startService(Intent(applicationContext, MusicSharingService::class.java))

            setupActiveSessionsControl()
            musicSharingController.onCreate()
            connecter.isScrobbling.value = true
        }
        else{
            Log.e(TAG, "Service stopped : No permission granted")
            stopSelf()
        }
    }

    fun stopSharing(){
        connecter.isScrobbling.value = false

        musicSharingController.onDestroy()

        val sessionManager = getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager
        sessionManager.removeOnActiveSessionsChangedListener(onActiveSessionsChangedListener)

        stopSelf()
    }



    //   *   *   *     *   *   *     *   *   *



    private fun setupActiveSessionsControl(){
        val manager = getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager
        val componentName = ComponentName(applicationContext, NotificationListenerService::class.java)

        try {
            manager.addOnActiveSessionsChangedListener(onActiveSessionsChangedListener, componentName)

            val controllers = manager.getActiveSessions(componentName)
            updateSessionsControl(controllers)

        } catch (e: Exception) {
            Log.e(TAG, "Service stopped with exception : ${e.message.toString()}")
            stopSelf()
        }
    }

    private fun updateSessionsControl(controllers: List<MediaController>?){
        if(controllers == null || controllers.isEmpty())return
        musicSharingController.updateMediaControllers(controllers)
    }

    //   *   *   *     *   *   *     *   *   *

    override fun onCreate() {
        inject()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
    }

    //   *   *   *     *   *   *     *   *   *

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val startedFromNotification =   intent?.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false) ?: false
        if(startedFromNotification){ stopSharing() }
        return START_NOT_STICKY
    }

    private val iBinder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder? {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        stopForeground(true)
        return iBinder
    }

    override fun onRebind(intent: Intent?) {
        stopForeground(true)
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        startForeground(
            NotificationUtil.NL_SERVICE_NOTIFICATION_ID,
            NotificationUtil.create(this)
        )
        return true
    }

    inner class LocalBinder : Binder(){
        val service : MusicSharingService
            get() = this@MusicSharingService
    }

    private fun inject() = with((application as BaseApplication).appComponent){
        this.inject(this@MusicSharingService)
    }

    companion object{
        private const val TAG = "TAG MusicSharingService"
    }
}