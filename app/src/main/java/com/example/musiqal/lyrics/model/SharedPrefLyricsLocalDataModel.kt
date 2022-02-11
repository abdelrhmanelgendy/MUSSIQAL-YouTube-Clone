package com.example.musiqal.lyrics.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class SharedPrefLyricsLocalDataModel(
    var songName: String,
    var songDuration: String,
    var songLyrics: String,
    var songThumbnails: String,
    var videoId: String,
):Serializable