package com.example.musiqal.util

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RemoteControlClient
import android.os.Build
import android.util.Log
import com.example.musiqal.customeMusicPlayer.musicNotification.DummyReciever
import com.example.musiqal.customeMusicPlayer.musicNotification.NotificationReciever
import com.example.musiqal.customeMusicPlayer.musicNotification.NotificationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    companion object {
        val CHANNEL_PLAYING_MUSIC_ID = "music"

    }


    override fun onCreate() {
        super.onCreate()
        createNotificationCahnnel()
        Log.d("BaseApplication", "onCreate: ")
        createHeadSetReciever()
//        customeMusicPlayback = CustomeMusicPlayback(this)

    }

    private fun createHeadSetReciever() {

//        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
//        var remoteComponent = ComponentName(this, NotificationReciever::class.java)
//        audioManager.registerMediaButtonEventReceiver(remoteComponent)
//        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
//        mediaButtonIntent.component = remoteComponent
//        val mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0)
//        val remoteControlClient = RemoteControlClient(mediaPendingIntent)
//        audioManager.registerRemoteControlClient(remoteControlClient)
//        remoteControlClient.setTransportControlFlags(
//            RemoteControlClient.FLAG_KEY_MEDIA_PLAY or RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
//                    or RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE or RemoteControlClient.FLAG_KEY_MEDIA_STOP
//                    or RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS or RemoteControlClient.FLAG_KEY_MEDIA_NEXT
//        )
////        val notificationReciever = NotificationReciever()
//        val headSetPluginFilter= IntentFilter(Intent.ACTION_HEADSET_PLUG)
//        registerReceiver(notificationReciever,headSetPluginFilter)


    }

    private fun createNotificationCahnnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel1 =
                NotificationChannel(
                    CHANNEL_PLAYING_MUSIC_ID,
                    "play song",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationChannel1.description = "start any song with custome notification"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(notificationChannel1))
        }
    }
}