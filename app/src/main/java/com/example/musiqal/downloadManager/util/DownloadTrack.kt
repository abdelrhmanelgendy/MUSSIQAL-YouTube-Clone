package com.example.musiqal.downloadManager.util

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musiqal.R
import com.example.musiqal.downloadManager.data.DownloadInfo
import com.example.musiqal.downloadManager.downloadBottomSheet.DownLoadInfoBottomSheet
import com.example.musiqal.downloadManager.source.core.DownloadManagerPro
import com.example.musiqal.downloadManager.source.core.enums.QueueSort
import com.example.musiqal.downloadManager.source.report.listener.DownloadManagerListener
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultDataItem
import com.google.gson.Gson
import java.io.File

class DownloadTrack(val context: Context, val urlInfo: DownloadInfo) : OnFilterationSuccess,
    OnSelectedTrackClickedListener, DownloadManagerListener {
    private val TAG = "DownloadTrack11"

    init {
        getFileInfo()

    }

    fun getFileInfo() {
        DownloadInfoExtractor(context, this).extractVideosUrlsByVideoLink(urlInfo)
    }

    override fun onSuccess(dataItems: List<YouTubeDlExtractorResultDataItem>) {
        val downloadFragment = DownLoadInfoBottomSheet.newInstance(Gson().toJson(dataItems), this)
        downloadFragment.show((context as AppCompatActivity).supportFragmentManager, "sx")
    }

    override fun onTrackSeelcted(currentSelectedItem: YouTubeDlExtractorResultDataItem) {
        startDownLoad(currentSelectedItem)
        Log.d(TAG, "onTrackSeelcted: " + currentSelectedItem)
    }

//    private fun startDownLoad(currentSelectedItem: YouTubeDlExtractorResultDataItem) {
    private fun startDownLoad(songData: YouTubeDlExtractorResultDataItem) {

    val link =
        songData.url
    val downloadManagerPro = DownloadManagerPro(context)
    val cashFile =
        File(Environment.DIRECTORY_DOWNLOADS + "/" + context.resources.getString(R.string.app_name))
    if (!cashFile.exists()) {
        Log.d(TAG, "onCreate: chash path ${cashFile.mkdir()}")
    }
    downloadManagerPro.init(cashFile.path, 1, this)
    val addTask = downloadManagerPro.addTask(songData.videoTitle+".mp3", link, false, true)
    val mTaskId = addTask
    


        downloadManagerPro.startQueueDownload(1, QueueSort.HighPriority)
        downloadManagerPro.startDownload(
            mTaskId
        )
        
        
        
        
        


////        val fileName = getFileTitleWithExt(currentSelectedItem)
//        val fileName = songName+".mp3"
//        val url = fileUrl
//        Log.d(TAG, "startDownLoad: " + fileName)
//        val downloadManagerPro = DownloadManagerPro(context)
//        val getPathFile = createFilePath()
//        val currentTask = downloadManagerPro.addTask(fileName, url, true, true)
//        initializeDownloadManager(downloadManagerPro, getPathFile, currentTask)
//
//
//        downloadManagerPro.downloadTasksInSameState(TaskStates.INIT)
////        downloadManagerPro.startQueueDownload(2, QueueSort.HighPriority)
//        downloadManagerPro.startDownload(currentTask)

    }

    override fun OnDownloadStarted(taskId: Long) {
        Log.d(TAG, "OnDownloadStarted: $taskId")
    }

    override fun OnDownloadPaused(taskId: Long) {
        Log.d(TAG, "OnDownloadPaused: $taskId")
    }

    override fun onDownloadProcess(taskId: Long, percent: Double, downloadedLength: Long) {
        Log.d(TAG, "onDownloadProcess: $taskId   $percent   $downloadedLength")

    }

    override fun OnDownloadFinished(taskId: Long) {
        Log.d(TAG, "OnDownloadFinished: $taskId")
    }

    override fun OnDownloadRebuildStart(taskId: Long) {
        Log.d(TAG, "OnDownloadRebuildStart: $taskId")
    }

    override fun OnDownloadRebuildFinished(taskId: Long) {
        Log.d(TAG, "OnDownloadRebuildFinished: $taskId")
    }

    override fun OnDownloadCompleted(taskId: Long) {
        Log.d(TAG, "OnDownloadCompleted: $taskId")
    }

    override fun connectionLost(taskId: Long) {
    }


    private fun getFileTitleWithExt(currentSelectedItem: YouTubeDlExtractorResultDataItem): String {
        if (currentSelectedItem.ext.equals("webm")) {
            return currentSelectedItem.videoTitle?.plus(".mp3")!!
        }
        return currentSelectedItem.videoTitle?.plus(currentSelectedItem.ext)!!

    }

//    private fun initializeDownloadManager(
//        downloadManagerPro: DownloadManagerPro,
//        filePath: String,
//        currentTask: Int
//    ) {
//        downloadManagerPro.init(filePath, 16, object : DownloadManagerListener {
//            override fun OnDownloadStarted(taskId: Long) {
//                if (taskId.toInt() == currentTask)
//                {
//                    Log.d(TAG, "OnDownloadStarted: ")
//                }
//            }
//
//            override fun OnDownloadPaused(taskId: Long) {
//                if (taskId.toInt() == currentTask)
//                {
//                    Log.d(TAG, "OnDownloadPaused: ")
//                }
//            }
//
//            override fun onDownloadProcess(taskId: Long, percent: Double, downloadedLength: Long) {
////                if (taskId.toInt() == currentTask)
////                {
//                    Log.d(TAG, "onDownloadProcess: "+percent+" "+downloadedLength)
////                }
//            }
//
//            override fun OnDownloadFinished(taskId: Long) {
////                if (taskId.toInt() == currentTask)
////                {
//                    Log.d(TAG, "OnDownloadFinished: ")
////                }
//            }
//
//            override fun OnDownloadRebuildStart(taskId: Long) {
//            }
//
//            override fun OnDownloadRebuildFinished(taskId: Long) {
//            }
//
//            override fun OnDownloadCompleted(taskId: Long) {
////                if (taskId.toInt() == currentTask)
////                {
//                    Log.d(TAG, "OnDownloadCompleted: ")
////                }
//            }
//
//            override fun connectionLost(taskId: Long) {
//                if (taskId.toInt() == currentTask)
//                {
//                    Log.d(TAG, "connectionLost: ")
//                }
//            }
//        })
//
//    }

    private fun createFilePath(): String {
        val cashFile =
            File(Environment.DIRECTORY_DOWNLOADS)
        if (!cashFile.exists()) {
            cashFile.mkdir()
        }
        return cashFile.path
    }

    private fun handleNotification(currentSelectedItem: YouTubeDlExtractorResultDataItem) {


    }
}
