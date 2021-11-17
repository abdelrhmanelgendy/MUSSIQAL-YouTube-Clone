package com.example.musiqal.youtubeAudioVideoExtractor.mvi

import com.example.musiqal.util.Resource
import com.example.musiqal.youtubeAudioVideoExtractor.model.LocalExtractedFileData
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultData

interface YoutubeExtractorDefaultRepository {

    suspend fun getAudios(videoUrl:String):Resource<YouTubeDlExtractorResultData>
    suspend fun insertExtraction(youtubeExtractionData:LocalExtractedFileData)
    suspend fun deleteExtraction(videoID:String)
    suspend fun findExtractionById(videoID:String):Resource<LocalExtractedFileData>
    suspend fun getAllExtractions():List<LocalExtractedFileData>

}