package com.example.musiqal.lyrics.mvi

import com.example.musiqal.lyrics.model.LyricsData

sealed class LyricsViewState {
    class Success(val lyrics: LyricsData) : LyricsViewState()
    object Idel : LyricsViewState()
    object Loading : LyricsViewState()
    class Failed(val errorMessgae: String) : LyricsViewState()
}
