package com.example.musiqal.datamodels.youtubeApiSearchForVideo

data class YoutubeApiRequest(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val regionCode: String
)