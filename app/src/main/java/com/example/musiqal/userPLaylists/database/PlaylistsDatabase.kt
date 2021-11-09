package com.example.musiqal.userPLaylists.database

import androidx.room.*
import com.example.musiqal.userPLaylists.model.UserPLayListitemConverter
import com.example.musiqal.userPLaylists.model.UserPlayList

@Database(entities = arrayOf(UserPlayList::class), version = 1)
@TypeConverters(UserPLayListitemConverter::class)
abstract class PlaylistsDatabase :RoomDatabase(){
    abstract fun getUserPLayListDao():UserPlaylistDao

}