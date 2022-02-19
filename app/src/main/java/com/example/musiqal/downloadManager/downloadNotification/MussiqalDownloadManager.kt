package com.example.musiqal.downloadManager.downloadNotification

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.musiqal.R
import kotlin.random.Random
import android.os.Environment
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.musiqal.downloadManager.source.core.DownloadManagerPro
import com.example.musiqal.downloadManager.source.core.enums.QueueSort
import com.example.musiqal.downloadManager.source.core.enums.TaskStates
import com.example.musiqal.downloadManager.source.report.listener.DownloadManagerListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.net.URL


class MussiqalDownloadManager() : Service(), DownloadManagerListener {
    private val TAG = "MussiqalDownloadManager"
    var id: Int = 0
    var fileLength: String = ""
    var mTaskId: Int = -1
    lateinit var fileName: String
    lateinit var fileDownloadUrl: String
    lateinit var fileImageUrl: String

    companion object {
        val FILE_NAME: String = "file_name"
        val FILE_IMAGE_URL: String = "file_image"
        val FILE_DOWNLOAD_URL: String = "file_url"
    }

    val NOTIFICATION_TITLE = "Mussiqal Download Manager"
    lateinit var notification: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManager
    lateinit var context: Context
    fun initialize(fileName: String) {
        notification =
            NotificationCompat.Builder(context, MussiqalDownloadManagerChannels.DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.musiqal_logo_large)
                .setContentTitle(NOTIFICATION_TITLE)
                .setProgress(100, 0, true)
                .setOnlyAlertOnce(true)
                .setContentTitle("pending")
                .setContentText(fileName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.BLACK)
                .setOngoing(true)
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!::notification.isInitialized) {
            val fileName = intent?.getStringExtra(FILE_NAME)
            val fileUrl = intent?.getStringExtra(FILE_DOWNLOAD_URL)
            val imageUrl = intent?.getStringExtra(FILE_IMAGE_URL)
            this.context = this
            this.fileName = fileName!!
            this.fileDownloadUrl = fileUrl!!
            this.fileImageUrl = imageUrl!!
            this.id = Random.nextInt(10160160)
            initialize(fileName)
            startForeground(id, notification.build())
            downLoad(fileName, fileImageUrl, fileUrl)
            getImageBitmapAndAssignToNotification(imageUrl)


        }
        return START_STICKY

    }

    private fun getImageBitmapAndAssignToNotification(imageUrl: String) {
        Glide.with(context).asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    updateNotificationIcon(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }

            })

    }

    private fun updateNotificationIcon(resource: Bitmap) {
        notification.setLargeIcon(resource)
        notificationNotify()

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun update(
        maxProgress: Int,
        progress: Int,
        contentTitleProcess: String?,
        contentTextTimes: String?,
        subTxt: String?,
        onGoinig: Boolean
    ) {
        notification
            .setProgress(maxProgress, progress, false)
            .setSubText(subTxt)
            .setContentTitle(contentTitleProcess)
            .setContentText(contentTextTimes)
            .setOngoing(onGoinig)
        notificationNotify()
    }

    fun notificationNotify() {
        notificationManager.notify(id, notification.build())
    }

    fun downLoad(fileName: String, fileImageUrl: String, fileUrl: String) {
        val downloadManagerPro = DownloadManagerPro(context)
        val cashFile =
            File(Environment.DIRECTORY_DOWNLOADS + "/" + resources.getString(R.string.app_name))
        if (!cashFile.exists()) {
            Log.d(TAG, "onCreate: chash path ${cashFile.mkdir()}")
        }
        downloadManagerPro.init(cashFile.path, 1, this)
//        val nextUrl =urls.get(java.util.Random().nextInt(urls.size))
        val nextUrl =
            fileUrl

        CoroutineScope(Dispatchers.IO)
            .launch {
                val url = URL(nextUrl)
                val contentLength = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    url.openConnection().contentLengthLong
                } else {
                    url.openConnection().contentLength
                }
                fileLength = bytesIntoHumanReadable(contentLength.toLong())
            }

        val addTask = downloadManagerPro.addTask(
            fileName,
            nextUrl,
            false,
            true
        )
        mTaskId = addTask
        downloadManagerPro.downloadTasksInSameState(TaskStates.INIT)
        downloadManagerPro.downloadTasksInSameState(TaskStates.DOWNLOADING)
        downloadManagerPro.startQueueDownload(1, QueueSort.HighPriority)
        downloadManagerPro.startDownload(
            mTaskId
        )
    }

    override fun OnDownloadStarted(taskId: Long) {
        Log.d(TAG, "OnDownloadStarted: "+taskId)
        update(100, 0, fileName, "", "0%", false)
    }

    override fun OnDownloadPaused(taskId: Long) {
    }

    override fun onDownloadProcess(taskId: Long, percent: Double, downloadedLength: Long) {

        Log.d(TAG, "downLoad: size: " + downloadedLength)
        Log.d(TAG, "onDownloadProcess: "+percent)
        update(
            100,
            percent.toInt(),
            fileName,
            bytesIntoHumanReadable(downloadedLength).toString() + "/" + fileLength,
            percent.toInt().toString() + "%",
            false
        )
    }

    override fun OnDownloadFinished(taskId: Long) {
    }

    override fun OnDownloadRebuildStart(taskId: Long) {

    }

    override fun OnDownloadRebuildFinished(taskId: Long) {

    }

    override fun OnDownloadCompleted(taskId: Long) {
        stopSelf()
        showCompleteNotification()
    }

    private fun showCompleteNotification() {
        val completeNotification =
            NotificationCompat.Builder(context, MussiqalDownloadManagerChannels.DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.musiqal_logo_large)
                .setContentTitle(NOTIFICATION_TITLE)
                .setProgress(0, 0, false)
                .setOnlyAlertOnce(true)
                .setContentTitle(this.fileName)
                .setContentText("download complete")
                .setSubText("")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.BLACK)
                .setOngoing(false)
        val completeNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        completeNotificationManager.notify(
            System.currentTimeMillis().toInt(),
            completeNotification.build()
        )

    }

    override fun connectionLost(taskId: Long) {

    }


    private fun bytesIntoHumanReadable(bytes: Long): String {
        val kilobyte: Long = 1024
        val megabyte = kilobyte * 1024
        val gigabyte = megabyte * 1024
        val terabyte = gigabyte * 1024
        return if (bytes >= 0 && bytes < kilobyte) {
            "$bytes B"
        } else if (bytes >= kilobyte && bytes < megabyte) {
            (bytes / kilobyte).toString() + " KB"
        } else if (bytes >= megabyte && bytes < gigabyte) {
            (bytes / megabyte).toString() + " MB"
        } else if (bytes >= gigabyte && bytes < terabyte) {
            (bytes / gigabyte).toString() + " GB"
        } else if (bytes >= terabyte) {
            (bytes / terabyte).toString() + " TB"
        } else {
            "$bytes Bytes"
        }
    }
}