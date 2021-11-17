package com.example.musiqal.datamodels.youtubeApiSearchForVideo

data class Item(
    val etag: String,
    val id: Id,
    val kind: String,
    val snippet: Snippet,
    var videoDuration:String

)