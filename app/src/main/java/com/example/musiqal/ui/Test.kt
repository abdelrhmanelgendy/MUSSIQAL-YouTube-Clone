package com.example.musiqal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.URLUtil
import androidx.room.util.FileUtil
import com.example.musiqal.R

import dagger.hilt.android.AndroidEntryPoint

import com.example.musiqal.databinding.ActivityTestBinding
import com.example.musiqal.downloadManager.source.core.DownloadManagerPro
import com.example.musiqal.downloadManager.source.core.enums.QueueSort
import com.example.musiqal.downloadManager.source.core.enums.TaskStates
import com.example.musiqal.downloadManager.source.report.listener.DownloadManagerListener
import java.io.File


@AndroidEntryPoint
class Test : AppCompatActivity(), DownloadManagerListener {
    private val TAG = "Test1111"
    lateinit var binding: ActivityTestBinding
    val urls = listOf(
        "https://cdns-preview-3.dzcdn.net/stream/c-3dc5d98ddf254fe1c30f650551670d33-2.mp3",
        "https://cdns-preview-6.dzcdn.net/stream/c-671b7c69e2c36d6ba666ade79b9b84de-6.mp3",
        "https://cdns-preview-2.dzcdn.net/stream/c-2162cc7ffa8325e5a2c8e003f7f3ba07-6.mp3"
    )
    var mTaskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val downloadManagerPro = DownloadManagerPro(this)
        val cashFile =
            File(Environment.DIRECTORY_DOWNLOADS + "/" + resources.getString(R.string.app_name))
        if (!cashFile.exists()) {
            Log.d(TAG, "onCreate: chash path ${cashFile.mkdir()}")
        }
        downloadManagerPro.init(cashFile.path, 1, this)
        val addTask = downloadManagerPro.addTask(getSongName(urls.get(0)), urls.get(0), false, true)
        mTaskId = addTask
        downloadManagerPro.downloadTasksInSameState(TaskStates.INIT)
        downloadManagerPro.downloadTasksInSameState(TaskStates.DOWNLOADING).forEach { i-> Log.d(TAG, "onCreate: "+i.toString()) }
        downloadManagerPro.downloadTasksInSameState(TaskStates.INIT).forEach { i-> Log.d(TAG, "onCreate: "+i.toString()) }
        downloadManagerPro.downloadTasksInSameState(TaskStates.DOWNLOAD_FINISHED).forEach { i-> Log.d(TAG, "onCreate: "+i.toString()) }
        downloadManagerPro.downloadTasksInSameState(TaskStates.END).forEach { i-> Log.d(TAG, "onCreate: "+i.toString()) }
        downloadManagerPro.downloadTasksInSameState(TaskStates.PAUSED).forEach { i-> Log.d(TAG, "onCreate: "+i.toString()) }
        downloadManagerPro.downloadTasksInSameState(TaskStates.READY).forEach { i-> Log.d(TAG, "onCreate: "+i.toString()) }
        binding.btnStart.setOnClickListener {
            downloadManagerPro.startQueueDownload(1, QueueSort.HighPriority)
            downloadManagerPro.startDownload(
                mTaskId
            )
        }
        binding.btnPause.setOnClickListener {
            downloadManagerPro.pauseDownload(mTaskId)
        }
        binding.btnReseum.setOnClickListener {
            downloadManagerPro.startDownload(mTaskId)
        }
        binding.btnStop.setOnClickListener {
            downloadManagerPro.pauseDownload(mTaskId)
            downloadManagerPro.delete(mTaskId, true)
        }


    }

    private fun getSongName(get: String): String {
        return URLUtil.guessFileName(get, null, null).toString() + ".mp3"
    }

    override fun OnDownloadStarted(taskId: Long) {
        Log.d(TAG, "OnDownloadStarted: $taskId")
    }

    override fun OnDownloadPaused(taskId: Long) {
        Log.d(TAG, "OnDownloadPaused: $taskId")
    }

    override fun onDownloadProcess(taskId: Long, percent: Double, downloadedLength: Long) {
        Log.d(TAG, "onDownloadProcess: $taskId   $percent   $downloadedLength")
        binding.progressBar.progress = percent.toInt()

        //fileModel
        //proccess
        //adapter.set(list.get(taskId))
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
}