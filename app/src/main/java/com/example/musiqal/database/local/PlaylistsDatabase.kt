package com.example.musiqal.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.musiqal.database.YoutubePlaylistTypeConverter
import com.example.musiqal.datamodels.StringConverter
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.datamodels.youtubeItemInList.ItemTypeConverter
import com.example.musiqal.search.database.SearchHistoryData

@Database(entities = arrayOf(YoutubeApiSearchForPlayListRequest::class,Item::class,SearchHistoryData::class,
), version = 1)
@TypeConverters(YoutubePlaylistTypeConverter::class,ItemTypeConverter::class,StringConverter::class)
abstract class PlaylistsDatabase : RoomDatabase() {
    abstract fun getPlayListsDao(): RandomPlaylistsDao
    abstract fun gethistoryOfPlayedItemDao(): HistoryOfPlayedItemDao
    abstract fun getHistoryOfSearchingDao(): HistoryOfSearchingDao

}