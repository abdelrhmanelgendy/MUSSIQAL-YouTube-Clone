package com.example.musiqal.youtubeAudioVideoExtractor.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "youtube_extracted_table")
data class LocalExtractedFileData constructor(var videId:String, var mp3Url:String)
{
    constructor():this("-1","-1")
    @PrimaryKey var p_Id:Int ?=null

}
