package com.example.musiqal.database.remote

import com.example.musiqal.lyrics.model.LyricsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LyricsApiSource {

    //https://api.lyrics.ovh/v1/sia/chandelier
    @GET("v1/{artistName}/{songName}")
   suspend fun getMusicLyrics(
        @Path("artistName") artistName: String,
        @Path("songName") songName: String
    ): Response<LyricsData>
}