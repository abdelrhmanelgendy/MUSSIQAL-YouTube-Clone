package com.example.musiqal.downloadManager.ui.adapters

data class DownloadItemsInfo(
     var filename: String,
     var fileId: String,
     var fileSize: Long,
     var downloadedSize: Long,
     var trackImage: String,
     var progress: Double,
     val state:Int

)
