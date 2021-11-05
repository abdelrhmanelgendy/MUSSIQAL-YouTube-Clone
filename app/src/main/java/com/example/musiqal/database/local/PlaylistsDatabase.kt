package com.example.musiqal.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.musiqal.databasesTypeConverters.YoutubePlaylistTypeConverter
import com.example.musiqal.models.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest
import com.example.musiqal.models.youtubeItemInList.Item
import com.example.musiqal.models.youtubeItemInList.ItemTypeConverter
import com.example.musiqal.search.database.SearchHistoryData

@Database(entities = arrayOf(YoutubeApiSearchForPlayListRequest::class,Item::class,SearchHistoryData::class), version = 1)
@TypeConverters(YoutubePlaylistTypeConverter::class,ItemTypeConverter::class)
abstract class PlaylistsDatabase : RoomDatabase() {
    abstract fun getPlayListsDao(): RandomPlaylistsDao
    abstract fun gethistoryOfPlayedItemDao(): HistoryOfPlayedItemDao
    abstract fun getHistoryOfSearchingDao(): HistoryOfSearchingDao
}