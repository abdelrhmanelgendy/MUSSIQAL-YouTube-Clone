package com.example.musiqal.search.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_of_searching_on_youtube")
data class SearchHistoryData(
    val searchTitle: String,
    val searchHistory: String
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    constructor():this("","")
}
