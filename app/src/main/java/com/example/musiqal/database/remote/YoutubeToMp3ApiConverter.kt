package com.example.musiqal.database.remote

import com.example.musiqal.datamodels.youtube.converter.toaudio.YoutubeMp3ConverterData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YoutubeToMp3ApiConverter {
    //https://youtube-mp36.p.rapidapi.com/dl?id=V1Pl8CzNzCw
    @GET("dl")
   suspend fun getMp3Url(
        @Header("x-rapidapi-host") rapidHost: String,
        @Header("x-rapidapi-key") rapidKey: String,
        @Query("id") videoId: String
    ):Response<YoutubeMp3ConverterData>
}