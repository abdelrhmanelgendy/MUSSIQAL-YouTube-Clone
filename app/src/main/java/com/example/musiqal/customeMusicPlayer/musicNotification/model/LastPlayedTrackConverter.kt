package com.example.musiqal.customeMusicPlayer.musicNotification.model

import com.google.gson.Gson

class LastPlayedTrackConverter {
    val gson = Gson()
    fun convertToPlayedTrack(track:String): LastPlayedTrack
    {
        return gson.fromJson(track, LastPlayedTrack::class.java)
    }

    fun convertToStringTrack(lastPlayedTrack: LastPlayedTrack):String
    {
        return gson.toJson(lastPlayedTrack)
    }
}