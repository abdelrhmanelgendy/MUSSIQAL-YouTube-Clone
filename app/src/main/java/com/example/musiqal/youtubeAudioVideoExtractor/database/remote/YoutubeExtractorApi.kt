package com.example.musiqal.youtubeAudioVideoExtractor.database.remote


import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultData
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface YoutubeExtractorApi {

    @FormUrlEncoded
    @POST("audio")
    suspend fun getAudios(@Field("v1") videoUrl: String): Response<YouTubeDlExtractorResultData>



}