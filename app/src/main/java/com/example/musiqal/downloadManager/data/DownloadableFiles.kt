package com.example.musiqal.downloadManager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musiqal.datamodels.youtubeItemInList.Thumbnails

@Entity(tableName = "Downloadable_files")
data class DownloadableFiles(
    val downloadManagerId: Int,
    val trackName: String,
    val trackVideoId: String,
    val downLoadState:Int,
    val filePathInStorage:String,
    val thumbnails: String,
){
    @PrimaryKey(autoGenerate = true)var uniqueId:Int?=null

}

