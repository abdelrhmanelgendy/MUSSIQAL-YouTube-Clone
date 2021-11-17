package com.example.musiqal.youtubeAudioVideoExtractor.database.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.musiqal.youtubeAudioVideoExtractor.model.LocalExtractedFileData

@Dao
interface YoutubeExtractedFileDao {
    @Insert
    suspend fun insertNewExtraction(extractedFileData: LocalExtractedFileData)

    @Query("delete from youtube_extracted_table where videId=:videoID")
    suspend fun deleteExtraction(videoID: String)

    @Query("select * from youtube_extracted_table where videId=:videoID")
    suspend fun getExtractionByVideoId(videoID: String):LocalExtractedFileData

    @Query("select * from youtube_extracted_table")
    suspend fun getAllExtractions():List<LocalExtractedFileData>
}