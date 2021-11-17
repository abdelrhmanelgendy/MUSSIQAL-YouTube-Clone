package com.example.musiqal.datamodels.youtubeItemInList

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "hitory_of_played_track")
data class Item(
    @Ignore
    val etag: String,
    var id: String,
    @Ignore
    val kind: String,
    var snippet: Snippet,
    var playedDateInMillisSecond:String,
    var videoDuration:String="-1"

) {
    @PrimaryKey(autoGenerate = true)
    var playedTrackID: Int? = null

    constructor() : this(
        "-1", "-1", "-1", Snippet(
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
        ),"")

}