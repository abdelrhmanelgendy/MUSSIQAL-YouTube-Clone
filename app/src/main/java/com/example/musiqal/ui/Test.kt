package com.example.musiqal.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.URLUtil
import androidx.room.util.FileUtil
import com.example.musiqal.R

import dagger.hilt.android.AndroidEntryPoint

import com.example.musiqal.databinding.ActivityTestBinding
import com.example.musiqal.downloadManager.downloadNotification.MussiqalDownloadManager
import com.example.musiqal.downloadManager.source.core.DownloadManagerPro
import com.example.musiqal.downloadManager.source.core.enums.QueueSort
import com.example.musiqal.downloadManager.source.core.enums.TaskStates
import com.example.musiqal.downloadManager.source.report.listener.DownloadManagerListener
import java.io.File
import kotlin.random.Random


@AndroidEntryPoint
class Test : AppCompatActivity() {
    private val TAG = "Test1111"
    lateinit var binding: ActivityTestBinding
lateinit var mussiqalDownloadManager: MussiqalDownloadManager

    lateinit var foregroundService: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mussiqalDownloadManager = MussiqalDownloadManager()

        foregroundService = Intent(this, MussiqalDownloadManager::class.java)
        foregroundService.putExtra(
            "url",
            "https://cdn.oneesports.gg/cdn-data/2021/11/LeagueofLegends_ArcaneEnemySoundtrackSongImagineDragonsJID-min-450x253.jpg"
        )

        binding.btnStart.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(foregroundService)
            } else {
                startService(foregroundService)
            }
        }


    }






}