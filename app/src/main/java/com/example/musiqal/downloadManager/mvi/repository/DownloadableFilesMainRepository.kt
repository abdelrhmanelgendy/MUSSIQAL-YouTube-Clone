package com.example.musiqal.downloadManager.mvi.repository

import com.example.musiqal.downloadManager.data.DownloadableFiles
import com.example.musiqal.downloadManager.database.DownloadableFilesDao
import javax.inject.Inject

class DownloadableFilesMainRepository @Inject constructor(private val downloadableFilesDao: DownloadableFilesDao) :
    DownloadableFilesDefaultRepository {
    override suspend fun insertNewDownload(downloadableFiles: DownloadableFiles) {
        downloadableFilesDao.insertNewDownload(downloadableFiles)
    }

    override suspend fun changeTheStateOfFilesByUniquesId(uniqueId: Int, downLoadedState: Int) {
        downloadableFilesDao.changeTheStateOfFilesByTheUniqueId(uniqueId, downLoadedState)
    }


    override suspend fun changeTheStateOfFilesByDownloadId(downloadId: Int, downLoadedState: Int) {
        downloadableFilesDao.changeTheStateOfFilesByTheUniqueId(downloadId, downLoadedState)
    }


    override suspend fun getUniqueIdByDownloadManagerId(downlaodManagerId: Int):Int {

        return downloadableFilesDao.getUniqueIdByDownloadManagerId(downlaodManagerId);
    }

    override suspend fun deleteFilesFromDownload(uniqueId: Int) {
        downloadableFilesDao.deleteFilesFromDownload(uniqueId)
    }

    override suspend fun getAllFiles(): List<DownloadableFiles> {
        return downloadableFilesDao.getAllFiles()


    }

    override suspend fun getSpecificFileByUniqueId(uniqueId: Int): DownloadableFiles {
        return downloadableFilesDao.getSpecificFileByUniqueId(uniqueId)
    }

    override suspend fun getSpecificFileByDownloadId(downloadId: Int): DownloadableFiles {
        return downloadableFilesDao.getSpecificFileByDownloadId(downloadId)


    }

    override suspend fun getFilesByState(downLoadedState: Int): List<DownloadableFiles> {
        return downloadableFilesDao.getFilesByState(downLoadedState)
    }
}