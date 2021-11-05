package com.example.musiqal.lyrics.util


interface YoutubeLyricsListener {
    fun onSuccess(lyrics:String)
    fun onFailed(e: Throwable?)
}