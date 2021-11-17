package com.example.musiqal.userPLaylists.database

import androidx.room.*
import com.example.musiqal.userPLaylists.model.UserPLayListitemConverter
import com.example.musiqal.userPLaylists.model.UserPlayList
import com.example.musiqal.youtubeAudioVideoExtractor.database.local.YoutubeExtractedFileDao
import com.example.musiqal.youtubeAudioVideoExtractor.model.LocalExtractedFileData

@Database(entities = arrayOf(UserPlayList::class,LocalExtractedFileData::class), version = 1)
@TypeConverters(UserPLayListitemConverter::class)
abstract class PlaylistsDatabase :RoomDatabase(){
    abstract fun getUserPLayListDao():UserPlaylistDao
    abstract fun getExtractionDao():YoutubeExtractedFileDao

}