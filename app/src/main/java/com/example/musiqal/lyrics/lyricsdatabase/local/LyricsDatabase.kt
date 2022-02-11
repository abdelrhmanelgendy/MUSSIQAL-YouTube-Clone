package com.example.musiqal.lyrics.lyricsdatabase.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musiqal.lyrics.model.LyricsLocalDataModel

@Database(entities = [LyricsLocalDataModel::class], version = 2)
abstract class LyricsDatabase : RoomDatabase() {
    abstract fun getLocalLyricsDao(): LyricsDatabaseDao

}