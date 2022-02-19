package com.example.musiqal.downloadManager.downloadNotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MussiqalDownloadManagerChannels {

    companion object
    {
        val DOWNLOAD_CHANNEL="download_channel"
        val DOWNLOAD_CHANNEL_ID="download"
    }

    lateinit var downloadChannel:NotificationChannel
    @RequiresApi(Build.VERSION_CODES.O)
    public fun createNotificationChannelAPI24(context: Context)
    {
      val notificationManager:NotificationManager=
          context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
        downloadChannel= NotificationChannel(DOWNLOAD_CHANNEL_ID,DOWNLOAD_CHANNEL,NotificationManager.IMPORTANCE_HIGH)
        downloadChannel.description="downloading single file and multiple file at a time"
        notificationManager.createNotificationChannel(downloadChannel)
    }

}