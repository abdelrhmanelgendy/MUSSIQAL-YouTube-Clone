package com.example.musiqal.downloadManager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.musiqal.downloadManager.data.DownLoadedState
import com.example.musiqal.downloadManager.data.DownloadableFiles

@Dao
public interface DownloadableFilesDao {

    @Insert
    suspend fun insertNewDownload(downloadableFiles: DownloadableFiles)

    @Query("update Downloadable_files set downLoadState =:downLoadedState where uniqueId=:uniqueId")
    suspend fun changeTheStateOfFilesByTheUniqueId(uniqueId:Int, downLoadedState: Int)


    @Query("update Downloadable_files set downLoadState =:downLoadedState where downloadManagerId=:downloadId")
    suspend fun changeTheStateOfFilesByDownloadId(downloadId: Int, downLoadedState: Int)

    @Query("select uniqueId from Downloadable_files where downloadManagerId=:downLoadedState")
    suspend fun getUniqueIdByDownloadManagerId( downLoadedState: Int):Int


    @Query("delete from Downloadable_files where uniqueId =:uniqueId")
    suspend fun deleteFilesFromDownload(uniqueId:Int)

    @Query("select * from Downloadable_files")
    suspend fun getAllFiles():List<DownloadableFiles>

    @Query("select * from Downloadable_files where uniqueId =:uniqueId")
    suspend fun getSpecificFileByUniqueId(uniqueId: Int):DownloadableFiles

    @Query("select * from Downloadable_files where downloadManagerId =:downloadId")
    suspend fun getSpecificFileByDownloadId(downloadId: Int):DownloadableFiles

    @Query("select * from Downloadable_files where downLoadState=:downLoadedState")
    suspend fun getFilesByState(downLoadedState: Int):List<DownloadableFiles>

}