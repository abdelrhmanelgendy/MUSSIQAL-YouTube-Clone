package com.example.musiqal.models.youtubeApiChannel

data class YoutubeCategoryRequest(
    val etag: String,
    val items: List<Item>,
    val kind: String
)