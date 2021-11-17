package com.example.musiqal.database.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest

@Dao
interface RandomPlaylistsDao {

    @Insert
    suspend fun insertPlayList(youtubeApiSearchForPlayListRequest: YoutubeApiSearchForPlayListRequest)

    @Query("delete from youtubeRandomPlayList where id=:id")
    suspend fun deleteItem(id:Int)

    @Query("select * from youtubeRandomPlayList where id=:id")
    suspend fun getItem(id: Int): YoutubeApiSearchForPlayListRequest


}