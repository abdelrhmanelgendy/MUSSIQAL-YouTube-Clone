package com.example.musiqal.downloadManager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musiqal.downloadManager.data.DownloadableFiles


@Database(entities = [DownloadableFiles::class],version = 1)
public abstract class DownloadableFilesDataBase :RoomDatabase(){
    companion object
    {
        val DownloadableFilesDataBaseName:String="DownloadableFiles_dataBase"
    }
    public abstract fun getDownloadableFilesDao():DownloadableFilesDao
}