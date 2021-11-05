package com.example.musiqal.models.youtubeItemInList

data class YoutubeVideosInPlaylistRequest(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo
)