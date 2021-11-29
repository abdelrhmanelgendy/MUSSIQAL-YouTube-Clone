package com.example.musiqal.downloadManager.data

import com.example.musiqal.datamodels.youtubeItemInList.Thumbnails

data class DownloadInfo(
    var videoTitle: String, var videoDuration: String, val videoId: String,
//    val thumbnails: Thumbnails
)
