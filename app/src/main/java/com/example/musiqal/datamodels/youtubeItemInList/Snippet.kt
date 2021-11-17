package com.example.musiqal.datamodels.youtubeItemInList

data class Snippet(
    val channelId: String,
    val channelTitle: String,
    val description: String,
    val playlistId: String,
    val position: Int,
    val publishedAt: String,
    val resourceId: ResourceId,
    val thumbnails: Thumbnails,
    val title: String,
    val videoOwnerChannelId: String,
    val videoOwnerChannelTitle: String
)