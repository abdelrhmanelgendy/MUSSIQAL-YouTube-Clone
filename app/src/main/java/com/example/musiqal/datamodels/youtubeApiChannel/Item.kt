package com.example.musiqal.datamodels.youtubeApiChannel

data class Item(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet
)