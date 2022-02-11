package com.example.musiqal.lyrics.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "M_Lyrics_table")
data class LyricsLocalDataModel(
    var songName: String,
    var songDuration: String,
    var songLyrics: String,
    var songThumbnails: String,
    var videoId: String,
    @PrimaryKey(autoGenerate = true) var id: Int? = null
) {

}