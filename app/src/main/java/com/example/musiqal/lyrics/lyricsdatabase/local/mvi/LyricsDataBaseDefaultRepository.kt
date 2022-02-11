package com.example.musiqal.lyrics.lyricsdatabase.local.mvi

import com.example.musiqal.lyrics.model.LyricsLocalDataModel
import com.example.musiqal.util.Resource

interface LyricsDataBaseDefaultRepository {
    suspend fun insertLyricsData(lyricsLocalDataModel: LyricsLocalDataModel)
    suspend fun getAllLyricsData(): Resource<List<LyricsLocalDataModel>>
    suspend fun deleteLyricsData(trackId: Int)
}