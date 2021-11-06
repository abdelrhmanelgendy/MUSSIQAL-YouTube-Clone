package com.example.musiqal.lyrics.mvi

import com.example.musiqal.database.remote.LyricsApiSource
import com.example.musiqal.lyrics.model.LyricsData
import com.example.musiqal.util.Resource
import java.lang.Exception
import javax.inject.Inject

class LyricsMainRepository @Inject constructor(val lyricsApiSource: LyricsApiSource):LyricsDefaultRepository{

    override suspend fun getSongLyrice(artistName: String, songName: String): Resource<LyricsData> {
        return try {
            val searchForLyrics = lyricsApiSource.getMusicLyrics(
                artistName, songName
            )
            if (searchForLyrics.isSuccessful) {
                return Resource.Success(searchForLyrics.body()!!)
            } else {
                return Resource.Failed(searchForLyrics.message().toString())
            }


        } catch (e: Exception) {
            Resource.Failed(e.message.toString())
        }
    }

}