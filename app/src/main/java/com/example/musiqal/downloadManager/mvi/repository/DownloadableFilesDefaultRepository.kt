package com.example.musiqal.downloadManager.mvi.repository

import androidx.room.Insert
import androidx.room.Query
import com.example.musiqal.downloadManager.data.DownloadableFiles

interface DownloadableFilesDefaultRepository {


    suspend fun insertNewDownload(downloadableFiles: DownloadableFiles)

    suspend fun changeTheStateOfFilesByUniquesId(uniqueId: Int, downLoadedState: Int)
    suspend fun changeTheStateOfFilesByDownloadId(downloadId: Int, downLoadedState: Int)
    suspend fun getUniqueIdByDownloadManagerId(downLoadedState: Int):Int

    suspend fun deleteFilesFromDownload(uniqueId: Int)

    suspend fun getAllFiles(): List<DownloadableFiles>

    suspend fun getSpecificFileByUniqueId(uniqueId: Int): DownloadableFiles

    suspend fun getSpecificFileByDownloadId(downloadId: Int): DownloadableFiles

    suspend fun getFilesByState(downLoadedState: Int): List<DownloadableFiles>
}