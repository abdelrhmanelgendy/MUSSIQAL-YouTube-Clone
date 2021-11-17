package com.example.musiqal.datamodels.youtubeApiChannel

data class YoutubeCategoryRequest(
    val etag: String,
    val items: List<Item>,
    val kind: String
)