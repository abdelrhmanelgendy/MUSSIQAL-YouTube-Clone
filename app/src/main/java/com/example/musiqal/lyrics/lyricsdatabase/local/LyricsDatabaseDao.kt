package com.example.musiqal.lyrics.lyricsdatabase.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.musiqal.lyrics.model.LyricsLocalDataModel

@Dao
interface LyricsDatabaseDao {

    @Query("insert into M_Lyrics_table(songName,songDuration,songLyrics,songThumbnails,videoId) values(:songName,:songDuration,:songLyrics,:songThumbnails,:videoId)")
    suspend fun insertNewTrackLyrics(songName:String,songDuration:String,songLyrics:String,songThumbnails:String,videoId:String)

//    @Query("select * from M_Lyrics_table order by id Asc")
//    suspend fun getAllTracksLyrics(): List<LyricsLocalDataModel>
//
//    @Query("delete from Lyrics_table where id =:id")
//    suspend fun deleteTrackLyrics(id: Int)
}