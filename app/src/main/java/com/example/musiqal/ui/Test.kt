package com.example.musiqal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.URLUtil
import com.example.musiqal.R

import dagger.hilt.android.AndroidEntryPoint

import com.example.musiqal.databinding.ActivityTestBinding
import com.example.musiqal.downloadManager.downloadBottomSheet.DownLoadInfoBottomSheet
import com.example.musiqal.downloadManager.source.core.DownloadManagerPro
import com.example.musiqal.downloadManager.source.report.listener.DownloadManagerListener
import com.example.musiqal.downloadManager.util.DownloadTrack
import com.example.musiqal.downloadManager.util.OnFilterationSuccess
import com.example.musiqal.downloadManager.util.OnSelectedTrackClickedListener
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultDataItem
import com.google.gson.Gson
import java.io.File


@AndroidEntryPoint
class Test : AppCompatActivity(), DownloadManagerListener, OnFilterationSuccess,
    OnSelectedTrackClickedListener {
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

        val link =
            "https://r4---sn-p5qlsny6.googlevideo.com/videoplayback?expire=1638083812&ei=hNiiYb2LFomohwazka-IBw&ip=3.91.157.194&id=o-AJjVHoXw3IlB5VwoWDQSAw7bwgzSl4b32XfFIkdpDkbU&itag=249&source=youtube&requiressl=yes&mh=Ws&mm=31%2C26&mn=sn-p5qlsny6%2Csn-vgqsknek&ms=au%2Conr&mv=m&mvi=4&pl=20&initcwndbps=976250&vprv=1&mime=audio%2Fwebm&ns=LfcpQZe3nGlxi3qRu7KdPhYG&gir=yes&clen=1416426&dur=222.101&lmt=1540482458071731&mt=1638061740&fvip=4&keepalive=yes&fexp=24001373%2C24007246&c=WEB&txp=5511222&n=3RUP_iRh_OYnQmkb&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AG3C_xAwRAIgUA0WphykJsLDwYYrqr6ykvRrdsAN2gxdGOKXHYbYaWACIEYea7qVaSdF30Qmf7bmy8fQywkspjQp6MDhlRVPpmAz&sig=AOq0QJ8wRQIgAUy-0B-9rDzFHg2L2D7HvglWgz9sUuITlit6p0ijuPECIQC6c4Npi1W1dhAP_4M84OVtWFEcZbfikcJkgrXTIjyAzQ=="
        val downloadManagerPro = DownloadManagerPro(this)
        val cashFile =
            File(Environment.DIRECTORY_DOWNLOADS + "/" + resources.getString(R.string.app_name))
        if (!cashFile.exists()) {
            Log.d(TAG, "onCreate: chash path ${cashFile.mkdir()}")
        }
        downloadManagerPro.init(cashFile.path, 1, this)
        val addTask = downloadManagerPro.addTask(getSongName(urls.get(0)), link, false, true)
        mTaskId = addTask
        binding.btnStart.setOnClickListener {


//            downloadManagerPro.startQueueDownload(1, QueueSort.HighPriority)
//            downloadManagerPro.startDownload(
//                mTaskId
//            )

//            val items = DownloadInfo("sia Alive", "PT11H54M48S", "L_LUpnjgPso")
//            DownloadInfoExtractor(this, this).extractVideosUrlsByVideoLink(items)
//
//            DownloadTrack(this, "sia-alive", link)
//

//

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

    override fun onSuccess(dataItems: List<YouTubeDlExtractorResultDataItem>) {
        val downloadFragment = DownLoadInfoBottomSheet.newInstance(Gson().toJson(dataItems), this)
        downloadFragment.show(supportFragmentManager, "sx")
    }

    override fun onTrackSeelcted(currentSelectedItem: YouTubeDlExtractorResultDataItem) {
        Log.d(TAG, "onTrackSeelcted: " + currentSelectedItem)
    }


}