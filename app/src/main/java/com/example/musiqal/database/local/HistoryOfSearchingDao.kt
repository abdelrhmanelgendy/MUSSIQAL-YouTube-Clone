package com.example.musiqal.database.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.musiqal.search.database.SearchHistoryData

@Dao
interface HistoryOfSearchingDao {

    @Insert
    suspend fun insertHistory(searchHistory: SearchHistoryData)



    @Query("select * from history_of_searching_on_youtube")
    suspend fun getAllSearchData(): List<SearchHistoryData>

    @Query("delete from history_of_searching_on_youtube")
    suspend fun deleteAllHistory()

    @Query("delete from history_of_searching_on_youtube where searchTitle=:title")
    suspend fun deleteHistoryByItsTitle(title:String)


}