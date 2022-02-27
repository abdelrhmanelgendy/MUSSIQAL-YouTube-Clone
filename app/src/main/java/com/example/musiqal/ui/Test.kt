package com.example.musiqal.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.URLUtil
import androidx.room.util.FileUtil
import com.example.musiqal.R
import com.example.musiqal.database.TracksImageSharedPref

import dagger.hilt.android.AndroidEntryPoint

import com.example.musiqal.databinding.ActivityTestBinding
import com.example.musiqal.downloadManager.androidDownloadManager.AndroidDownloadManager
import com.example.musiqal.downloadManager.data.DownloadInfo
import com.example.musiqal.downloadManager.downloadNotification.MussiqalDownloadManager
import com.example.musiqal.downloadManager.source.core.DownloadManagerPro
import com.example.musiqal.downloadManager.source.core.enums.QueueSort
import com.example.musiqal.downloadManager.source.core.enums.TaskStates
import com.example.musiqal.downloadManager.source.report.listener.DownloadManagerListener
import com.example.musiqal.downloadManager.ui.DownloadManagerActivity
import com.example.musiqal.downloadManager.util.DownloadMultipleTracks
import com.example.musiqal.downloadManager.util.DownloadTrack
import com.example.musiqal.downloadManager.util.OnDownloadListeners
import java.io.File
import java.lang.Exception
import kotlin.random.Random


@AndroidEntryPoint
class Test : AppCompatActivity(), DownloadManagerListener, OnDownloadListeners {
    private val TAG = "Test1111"
    lateinit var binding: ActivityTestBinding
    lateinit var mussiqalDownloadManager: MussiqalDownloadManager
    var started: Boolean = false
    lateinit var downloadManagerPro: DownloadManagerPro

    lateinit var foregroundService: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnStart.setOnClickListener {
////            val downloadManagerPro = DownloadManagerPro(this)
////            val downloadTasksInSameState = downloadManagerPro.downloadTasksInSameState(TaskStates.INIT)
////            Log.d(TAG, "onCreate: "+downloadTasksInSameState.toString())
        val url =
            "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4"
//            val downloadIntent = Intent(this, MussiqalDownloadManager::class.java)
//            downloadIntent.putExtra(MussiqalDownloadManager.FILE_NAME, System.currentTimeMillis().toString()+".mp3")
//            downloadIntent.putExtra(MussiqalDownloadManager.FILE_DOWNLOAD_URL, url)
//            downloadIntent.putExtra(MussiqalDownloadManager.FILE_IMAGE_URL, "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg")
//            if (isMyServiceRunning(MussiqalDownloadManager::class.java))
//            {
//                val addTask = DownloadManagerPro(this)
//                    .addTask(System.currentTimeMillis().toString()+".mp3", url, true, true)
//                TracksImageSharedPref(this)
//                    .saveImageWithId(addTask.toLong(),"https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg")
//            }
//            else
//            {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startForegroundService(downloadIntent)
//                } else {
//                    val startService = startService(downloadIntent)
//                }
//            }
//        }
        binding.btnStop.setOnClickListener {
            val urlsInfos: MutableList<DownloadInfo> = mutableListOf()
            urlsInfos.add(
                DownloadInfo(
                    "Clean Bandit - Symphony (feat. Zara Larsson) [Official Video]",
                    "",
                    "aatr_2MstrI"
                )
            )
//            urlsInfos.add(
               val info= DownloadInfo(
                    "Adele - Oh My God (Official Lyric Video)",
                    "",
                    "PfwIpoVMvNs"
                )
//            )
            DownloadTrack(this, info," listOf()")
//            val androidDownloadManager= AndroidDownloadManager(this)
//            androidDownloadManager.download(url,System.currentTimeMillis().toString())
        }
//
//        val url =
//            "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4"
//        val file =
//            File(Environment.DIRECTORY_DOWNLOADS + "/" + resources.getString(R.string.app_name))
//        DownloadTrack(this, DownloadInfo(System.))
//        downloadManagerPro = DownloadManagerPro(this)
//        downloadManagerPro.init(file.path, 1, this)
//        binding.btnStart.setOnClickListener {
//            val addTask = downloadManagerPro.addTask(
//                (System.currentTimeMillis().toString() + "m.mp3"),
//                url,
//                8,
//                false,
//                true
//            )
//            if (!started) {
//                downloadManagerPro.startDownload(addTask)
//                started = true
////                try {
////                    downloadManagerPro.startQueueDownload(1, QueueSort.oldestFirst)
////
////                } catch (e: Exception) {
//            }
////            }
////            downloadManagerPro.
//
//        }


    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun OnDownloadStarted(taskId: Long) {

        Log.d(TAG, "OnDownloadStarted: " + taskId)

    }

    override fun OnDownloadPaused(taskId: Long) {
        Log.d(TAG, "OnDownloadPaused: " + taskId)
    }

    override fun onDownloadProcess(taskId: Long, percent: Double, downloadedLength: Long) {
        Log.d(TAG, "onDownloadProcess: preogress " + percent + " " + taskId)
    }

    override fun OnDownloadFinished(taskId: Long) {
        Log.d(TAG, "OnDownloadFinished: " + taskId)

    }

    override fun OnDownloadRebuildStart(taskId: Long) {
    }

    override fun OnDownloadRebuildFinished(taskId: Long) {
    }

    override fun OnDownloadCompleted(taskId: Long) {
//        downloadManagerPro.startQueueDownload(1, QueueSort.oldestFirst)
        downloadManagerPro.startDownload((taskId + 1).toInt())
        Log.d(TAG, "OnDownloadCompleted: " + taskId)
    }

    override fun connectionLost(taskId: Long) {
    }

    override fun complete(id: Long) {
        TODO("Not yet implemented")
    }

    override fun started(id: Long) {
        TODO("Not yet implemented")
    }

    override fun progress(id: Long, progress: Double) {
        TODO("Not yet implemented")
    }

    override fun paused(id: Long) {
        TODO("Not yet implemented")
    }


}