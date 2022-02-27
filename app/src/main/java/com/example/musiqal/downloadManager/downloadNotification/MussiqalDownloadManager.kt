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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.musiqal.database.TracksImageSharedPref
import com.example.musiqal.downloadManager.source.core.DownloadManagerPro
import com.example.musiqal.downloadManager.source.core.enums.QueueSort
import com.example.musiqal.downloadManager.source.core.enums.TaskStates
import com.example.musiqal.downloadManager.source.report.listener.DownloadManagerListener
import com.example.musiqal.downloadManager.util.OnDownloadListeners
import com.example.musiqal.viewModels.MainViewModel
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


    lateinit var downloadManagerPro: DownloadManagerPro

    companion object {
         var downloadManagerListener: DownloadManagerListener?=null
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
            startDownlaodingNotification(this.id, fileName, fileUrl, imageUrl)

        }
        return START_STICKY

    }

    private fun startDownlaodingNotification(
        id: Int,
        fileName: String,
        fileUrl: String,
        imageUrl: String
    ) {

        initialize(fileName)
        startForeground(id, notification.build())
        downLoad(fileName, fileImageUrl, fileUrl)
        getImageBitmapAndAssignToNotification(imageUrl)
    }

    private fun getImageBitmapAndAssignToNotification(imageUrl: String) {
        Glide.with(context).asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    updateNotificationIcon(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
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
        downloadManagerPro = DownloadManagerPro(context)
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
//        downloadManagerPro.startQueueDownload(1, QueueSort.HighPriority)
        downloadManagerPro.startDownload(
            mTaskId
        )
    }

    override fun OnDownloadStarted(taskId: Long) {
        Log.d(TAG, "OnDownloadStarted: " + taskId)
        update(100, 0, fileName, "", "0%", false)
        if (downloadManagerListener!=null)
        {
            downloadManagerListener?.OnDownloadStarted(taskId)
        }
    }

    override fun OnDownloadPaused(taskId: Long) {
        update(0, 0, "pause01", "pause02", "pause03", true)


    }

    override fun onDownloadProcess(taskId: Long, percent: Double, downloadedLength: Long) {

        Log.d(TAG, "downLoad: size: " + downloadedLength)
        Log.d(TAG, "onDownloadProcess: " + percent)
        update(
            100,
            percent.toInt(),
            fileName,
            bytesIntoHumanReadable(downloadedLength).toString() + "/" + fileLength,
            percent.toInt().toString() + "%",
            false
        )

        if (downloadManagerListener!=null)
        {
            downloadManagerListener?.onDownloadProcess(taskId,percent,downloadedLength)
        }
    }

    override fun OnDownloadFinished(taskId: Long) {
        if (downloadManagerListener!=null)
        {
            downloadManagerListener?.OnDownloadFinished(taskId)
        }
    }

    override fun OnDownloadRebuildStart(taskId: Long) {
        if (downloadManagerListener!=null)
        {
            downloadManagerListener?.OnDownloadRebuildStart(taskId)
        }
    }

    override fun OnDownloadRebuildFinished(taskId: Long) {
        if (downloadManagerListener!=null)
        {
            downloadManagerListener?.OnDownloadRebuildFinished(taskId)
        }
    }

    override fun OnDownloadCompleted(taskId: Long) {
//        stopSelf()
        showCompleteNotification()
        if (downloadManagerListener!=null)
        {
            downloadManagerListener?.OnDownloadCompleted(taskId)
        }
//        onDownloadListeners.complete(taskId)

        val singleDownloadStatus = downloadManagerPro.singleDownloadStatus((taskId + 1).toInt())
        if (singleDownloadStatus.name!=null)
        {
            val name = singleDownloadStatus.name
            val url = singleDownloadStatus.url
            val imageUrl = TracksImageSharedPref(context)
                .getImageUrlByTaskId(taskId)
            val nextInt = Random.nextInt(50505050)
            this.id=nextInt
            this.fileDownloadUrl=url
            this.fileImageUrl=imageUrl
            startDownlaodingNotification(nextInt,name,url,imageUrl)
        }
        else
        {
            stopSelf()
        }

//
    }

    private fun downloadTheNextFile(taskId: Long) {


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