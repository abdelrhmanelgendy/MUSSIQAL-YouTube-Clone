package com.example.musiqal.viewModels.viewStates

sealed class LyricsViewState {
    class Success(val lyrics: String) : LyricsViewState()
    object Idel : LyricsViewState()
    object Loading : LyricsViewState()
    class Failed(val errorMessgae: String) : LyricsViewState()
}
