package com.example.musiqal.lyrics.mvi

import com.example.musiqal.lyrics.model.LyricsData
import com.example.musiqal.util.Resource

interface LyricsDefaultRepository {
    suspend fun getSongLyrice(artistName: String, songName: String): Resource<LyricsData>
}