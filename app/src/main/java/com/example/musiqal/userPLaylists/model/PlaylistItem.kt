package com.example.musiqal.userPLaylists.model

import com.example.musiqal.datamodels.youtubeItemInList.*

data class PlaylistItem(
    var etag: String,
    var id: String,
    var kind: String,
    var snippet: Snippet,
    var playedDateInMillisSecond: String
) {
    constructor() : this(
        "-1",
        "-1", "-1",
        Snippet(
            "1", "1",
            "1", "1", 1, "1",
            ResourceId(
                "", "",
            ), Thumbnails(
                Default(1, "", 1),
                High(1, "", 1),
                Maxres(1, "", 1),
                Medium(1, "", 1),
                Standard(1, "", 1)
            ),
            "", "", ""
        ),
        ""
    )

}

