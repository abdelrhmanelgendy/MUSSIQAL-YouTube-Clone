package com.example.musiqal.customeMusicPlayer.musicNotification

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.KeyEvent
import androidx.media.session.MediaButtonReceiver

class NotificationService : Service() {
    val NOTIFICATION_ID = 11

    companion object {
        lateinit var customeNotification: Notification

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, customeNotification)
        return START_STICKY

    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}