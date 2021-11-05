package com.example.musiqal.models.youtubeApiSearchForPlayList

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


data class Item(
    val etag: String,
    val id: Id,
    val kind: String,
    val snippet: Snippet,
    ) {


}
