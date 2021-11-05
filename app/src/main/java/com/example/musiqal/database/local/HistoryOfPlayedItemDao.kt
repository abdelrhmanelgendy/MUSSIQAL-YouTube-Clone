package com.example.musiqal.database.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.musiqal.models.youtubeItemInList.Item

@Dao
interface HistoryOfPlayedItemDao {

    @Insert
    suspend fun insertTrack(trackItem: Item)

    @Query("delete from hitory_of_played_track where playedTrackID =:trackID")
    suspend fun deleteTrack(trackID: Int)

    @Query("select * from hitory_of_played_track")
    suspend fun selectAllTracks(): List<Item>


}