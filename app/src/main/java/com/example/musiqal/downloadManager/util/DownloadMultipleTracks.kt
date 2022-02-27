package com.example.musiqal.downloadManager.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musiqal.downloadManager.data.DownLoadedState
import com.example.musiqal.downloadManager.data.DownloadInfo
import com.example.musiqal.downloadManager.data.DownloadableFiles
import com.example.musiqal.downloadManager.downloadBottomSheet.DownLoadInfoBottomSheet
import com.example.musiqal.downloadManager.downloadNotification.MussiqalDownloadManager
import com.example.musiqal.downloadManager.mvi.viewModels.DownloadableFilesViewModel
import com.example.musiqal.downloadManager.mvi.viewModels.intents.UniqueIdState
import com.example.musiqal.downloadManager.source.core.DownloadManagerPro
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultDataItem
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.io.File
import androidx.core.content.ContextCompat.getSystemService

import android.app.ActivityManager
import androidx.core.content.ContextCompat
import com.example.musiqal.database.TracksImageSharedPref
import com.example.musiqal.dialogs.SimpleYesOrNoDialog
import com.example.musiqal.downloadManager.androidDownloadManager.AndroidDownloadManager


class DownloadMultipleTracks(
    private  val context: Context,
    private val urlsInfos: List<DownloadInfo>,
    private val imageUrls: List<String>,
) : OnFilterationSuccess,
    OnSelectedTrackClickedListener, OnDownloadListeners {
    private val TAG = "DownloadTrack11"
    lateinit var allSongsList: MutableList<List<YouTubeDlExtractorResultDataItem>>

    init {
        allSongsList= mutableListOf()
        showProccessOfMultipleExtraction()
        getFileInfo()

    }
var currentIndex=0
//    lateinit var simpleYesOrNoDialog :SimpleYesOrNoDialog
    private fun showProccessOfMultipleExtraction() {
//        simpleYesOrNoDialog=SimpleYesOrNoDialog(context)
        val extractUrlMsg = "Extracting "
//        if (allSongsList==null||allSongsList.size==0)
        val trackInfo = urlsInfos.get(currentIndex)
//        simpleYesOrNoDialog.intialize(extractUrlMsg+trackInfo.videoTitle,(allSongsList.size.toString())+"/"+(urlsInfos.size.toString()), "", "", 0, false)
//        simpleYesOrNoDialog.show(false)

    }

   private fun getFileInfo() {

        urlsInfos.forEach {
            DownloadInfoExtractor(context, this,false).
            extractVideosUrlsByVideoLink(it)
        }
    }

    override fun onSuccess(dataItems: List<YouTubeDlExtractorResultDataItem>) {
        currentIndex+=1
        allSongsList.add(dataItems)
        Log.d(TAG, "onSuccess: allSongsList"+allSongsList.size)
//        simpleYesOrNoDialog.updateSubText(allSongsList.size.toString()+"/"+(urlsInfos.size.toString()))
//        if (urlsInfos.size>currentIndex)
//        {
//            Log.d(TAG, "onSuccessMM: "+urlsInfos.size)
//            Log.d(TAG, "onSuccessMM: "+currentIndex)
//            val trackInfo = urlsInfos.get(currentIndex)
//            val extractUrlMsg = "Extracting "
//
////            simpleYesOrNoDialog.updateMain(extractUrlMsg+trackInfo.videoTitle)
////
//        }
//        simpleYesOrNoDialog.updateMain(allSongsList.size.toString()+"/"+(urlsInfos.size.toString()))
        if (allSongsList.size == urlsInfos.size) {
            Log.d(TAG, "onSuccess: extraction complete"+allSongsList.size+"  "+urlsInfos.size)
//            simpleYesOrNoDialog.dismis()
        }
//        Log.d(TAG, "onSuccess: " + dataItems.size)
//        val downloadFragment =
//            DownLoadInfoBottomSheet.newInstance(Gson().toJson(dataItems), this, imageUrl)
//        downloadFragment.show((context as AppCompatActivity).supportFragmentManager, "sx")
    }

    override fun onTrackSeelcted(currentSelectedItem: YouTubeDlExtractorResultDataItem) {
//        startDownLoad(currentSelectedItem)
//        Log.d(TAG, "onTrackSeelcted: " + currentSelectedItem)
    }

    //    private fun startDownLoad(currentSelectedItem: YouTubeDlExtractorResultDataItem) {
    private fun startDownLoad(songData: YouTubeDlExtractorResultDataItem) {


        val androidDownloadManager = AndroidDownloadManager(context)
        androidDownloadManager.download(songData.url, songData.videoTitle)


//        val downloadableFile = DownloadableFiles(
//            -1,
//            songData.videoTitle!!,
//            urlInfo.videoId,
//            DownLoadedState.UnKnown.state,
//            "-1",
//            imageUrl
//        )
////        saveCurrentDownloadableToDataBase(downloadableFile)
//        val downloadIntent = Intent(context, MussiqalDownloadManager::class.java)
//        downloadIntent.putExtra(MussiqalDownloadManager.FILE_NAME, songData.videoTitle)
//        downloadIntent.putExtra(MussiqalDownloadManager.FILE_DOWNLOAD_URL, songData.url)
//        downloadIntent.putExtra(MussiqalDownloadManager.FILE_IMAGE_URL, imageUrl)
//        if (isMyServiceRunning(MussiqalDownloadManager::class.java))
//        {
//            val addTask = DownloadManagerPro(context)
//                .addTask(songData.videoTitle, songData.url, true, true)
//            TracksImageSharedPref(context)
//                .saveImageWithId(addTask.toLong(),imageUrl)
//        }
//        else
//        {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(downloadIntent)
//            } else {
//                val startService = context.startService(downloadIntent)
//            }
//        }


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

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
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

    private fun handleNotification(
        currentSelectedItem:
        YouTubeDlExtractorResultDataItem
    ) {


    }

    override fun complete(id: Long) {
//        downloadableFilesViewModel.changeTheStateOfFiles(id.toInt(),DownLoadedState.Success)

//getTheNextFileToDownload
//        downloadableFilesViewModel.getUniqueIdByDownloadManagerId(id.toInt())
//        CoroutineScope(Dispatchers.IO)
//            .launch {
//                downloadableFilesViewModel.uniqueIdByDownloadManagerIdStateFlow.collect {
//                    when (it) {
//                        is UniqueIdState.Success -> {
//
//                        }
//                        is UniqueIdState.Failed -> {
//
//                        }
//                    }
//                }
//            }
//        getTheNextDownload
    }

    override fun started(id: Long) {
//        downloadableFilesViewModel.getUniqueIdByDownloadManagerId(id.toInt())
//        CoroutineScope(Dispatchers.IO)
//            .launch {
//                downloadableFilesViewModel.uniqueIdByDownloadManagerIdStateFlow.collect {
//                    when (it) {
//                        is UniqueIdState.Success -> {
//
//                        }
//                        is UniqueIdState.Failed -> {
//
//                        }
//                    }
//                }
//            }
    }

    override fun progress(id: Long, progress: Double) {
    }

    override fun paused(id: Long) {
    }
}
