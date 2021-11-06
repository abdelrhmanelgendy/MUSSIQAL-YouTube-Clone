package com.example.musiqal.lyrics.util

interface OnLyricsFoundListener {
    fun onSuccess(lyrics: String)
    fun onFailed(error: String)

}
