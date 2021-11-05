package com.example.musiqal.models.youtubeApiSearchForVideo

data class Item(
    val etag: String,
    val id: Id,
    val kind: String,
    val snippet: Snippet,
    val videoDuration:String
)