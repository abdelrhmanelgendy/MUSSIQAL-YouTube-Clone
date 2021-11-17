package com.example.musiqal.youtubeAudioVideoExtractor.mvi

import com.example.musiqal.util.Resource
import com.example.musiqal.youtubeAudioVideoExtractor.database.local.YoutubeExtractedFileDao
import com.example.musiqal.youtubeAudioVideoExtractor.database.remote.YoutubeExtractorApi
import com.example.musiqal.youtubeAudioVideoExtractor.model.LocalExtractedFileData

import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultData
import javax.inject.Inject

class YoutubeExtractorMainRepository
@Inject constructor(
    private val youtubeExtractorApi:
    YoutubeExtractorApi,
    private val youtubeExtractedFileDao: YoutubeExtractedFileDao
) : YoutubeExtractorDefaultRepository {


    override suspend fun getAudios(videoUrl: String): Resource<YouTubeDlExtractorResultData> {
        return try {
            val result = youtubeExtractorApi.getAudios(videoUrl)
            if (result.isSuccessful) {
                Resource.Success(result.body()!!)
            } else {
                Resource.Failed(result.message())
            }
        } catch (e: Exception) {
            Resource.Failed(e.message!!)
        }
    }

    override suspend fun insertExtraction(youtubeExtractionData: LocalExtractedFileData) {
        youtubeExtractedFileDao.insertNewExtraction(youtubeExtractionData)
    }

    override suspend fun deleteExtraction(videoID: String) {
        youtubeExtractedFileDao.deleteExtraction(videoID)
    }

    override suspend fun findExtractionById(videoID: String): Resource<LocalExtractedFileData> {
        return try {
            Resource.Success(youtubeExtractedFileDao.getExtractionByVideoId(videoID))
        } catch (e: java.lang.Exception) {
            Resource.Failed(e.message!!)
        }
    }

    override suspend fun getAllExtractions(): List<LocalExtractedFileData> {
        return youtubeExtractedFileDao.getAllExtractions()
    }


}