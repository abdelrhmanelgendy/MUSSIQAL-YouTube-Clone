package com.example.musiqal.downloadManager.ui

import DownloadManagerRecyclerViewAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musiqal.R
import com.example.musiqal.database.TracksImageSharedPref
import com.example.musiqal.databinding.ActivityDownloadManagerBinding
import com.example.musiqal.downloadManager.data.DownloadInfo
import com.example.musiqal.downloadManager.downloadNotification.MussiqalDownloadManager
import com.example.musiqal.downloadManager.source.core.DownloadManagerPro
import com.example.musiqal.downloadManager.source.core.enums.TaskStates
import com.example.musiqal.downloadManager.source.report.ReportStructure
import com.example.musiqal.downloadManager.source.report.listener.DownloadManagerListener
import com.example.musiqal.downloadManager.ui.adapters.DownloadItemsInfo

lateinit var binding: ActivityDownloadManagerBinding
private const val TAG = "DownloadManagerActivity"

class DownloadManagerActivity : AppCompatActivity(), DownloadManagerListener {
    var downloadInfos: List<DownloadItemsInfo> = listOf()
    lateinit var downloadManagerRecyclerViewAdapter: DownloadManagerRecyclerViewAdapter
    lateinit var downloadManagerPro: DownloadManagerPro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        downloadManagerPro = DownloadManagerPro(this)
        getDataOfDownloadManager()
        MussiqalDownloadManager.downloadManagerListener = this
    }

    fun getDataOfDownloadManager() {
        val downloadTasksInSameState = downloadManagerPro.downloadTasksInSameState(TaskStates.DOWNLOADING)
        downloadTasksInSameState.addAll(downloadManagerPro.downloadTasksInSameState(TaskStates.INIT))
        downloadTasksInSameState.addAll(downloadManagerPro.downloadTasksInSameState(TaskStates.READY))
        downloadTasksInSameState.addAll(downloadManagerPro.downloadTasksInSameState(TaskStates.PAUSED))
        downloadTasksInSameState.addAll(downloadManagerPro.downloadTasksInSameState(TaskStates.END))
        downloadTasksInSameState.addAll(downloadManagerPro.downloadTasksInSameState(TaskStates.DOWNLOAD_FINISHED))
        downloadInfos = extractDownloadInfoFromReports(downloadTasksInSameState)
        initializeRecyclerViewAdapters(downloadInfos)

    }

    private fun initializeRecyclerViewAdapters(extractDownloadInfoFromReports: List<DownloadItemsInfo>) {

        downloadManagerRecyclerViewAdapter = DownloadManagerRecyclerViewAdapter(this)
        downloadManagerRecyclerViewAdapter.setList(extractDownloadInfoFromReports)
        binding.downloadManagerActivityRecyclerViewDownloads.post {
            binding.downloadManagerActivityRecyclerViewDownloads.layoutManager =
                LinearLayoutManager(this)
            binding.downloadManagerActivityRecyclerViewDownloads.adapter =
                downloadManagerRecyclerViewAdapter
        }


    }

    private fun extractDownloadInfoFromReports(downloadTasksInSameState: List<ReportStructure>): List<DownloadItemsInfo> {
        return downloadTasksInSameState.map { it ->
            DownloadItemsInfo(
                it.name,
                it.id.toString(),
                it.fileSize,
                it.downloadLength,
                getImageUrl(it.id),
                0.0,
                it.state
            )
        }
    }

    private fun getImageUrl(id: Int): String {
        return TracksImageSharedPref(this).getImageUrlByTaskId(id.toLong())
    }

    override fun OnDownloadStarted(taskId: Long) {
        getDataOfDownloadManager()

    }

    override fun OnDownloadPaused(taskId: Long) {

    }

    override fun onDownloadProcess(taskId: Long, percent: Double, downloadedLength: Long) {

        if (downloadInfos.size > 0) {
            downloadInfos.forEachIndexed { index, downloadItemsInfo ->
                Log.d(TAG, "onDownloadProcesstaskId: orifinal Id ="+taskId+" downloadItemsInfo " + downloadItemsInfo.fileId)

                if ((downloadItemsInfo.fileId).toInt() == taskId.toInt()) {
                    binding.downloadManagerActivityRecyclerViewDownloads.post {
                        downloadItemsInfo.progress = percent
                        downloadItemsInfo.downloadedSize = downloadedLength
                        downloadManagerRecyclerViewAdapter.updateItem(downloadInfos, index)

                    }

                }
            }
        }


    }

    override fun OnDownloadFinished(taskId: Long) {

    }

    override fun OnDownloadRebuildStart(taskId: Long) {

    }

    override fun OnDownloadRebuildFinished(taskId: Long) {

    }

    override fun OnDownloadCompleted(taskId: Long) {
        getDataOfDownloadManager()
    }

    override fun connectionLost(taskId: Long) {

    }
}