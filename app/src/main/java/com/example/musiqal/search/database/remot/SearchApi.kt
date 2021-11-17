package com.example.musiqal.search.database.remot

import com.example.musiqal.datamodels.youtubeApiSearchForVideo.YoutubeApiRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search")
   suspend fun searchForVideo(
        @Query("part") part: String,
        @Query("type") type: String,
        @Query("q") searchQuery: String,
        @Query("maxResults") maxResult: String,
        @Query("videoCategoryId") videoCategoryId: String,
        @Query("key") api_key: String
    ): Response<YoutubeApiRequest>


}