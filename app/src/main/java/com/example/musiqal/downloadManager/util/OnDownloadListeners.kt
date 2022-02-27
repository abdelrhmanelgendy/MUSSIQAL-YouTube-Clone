package com.example.musiqal.downloadManager.util

interface OnDownloadListeners {
    fun complete(id: Long)
    fun started(id: Long)
    fun progress(id: Long, progress: Double)
    fun paused(id: Long)

}