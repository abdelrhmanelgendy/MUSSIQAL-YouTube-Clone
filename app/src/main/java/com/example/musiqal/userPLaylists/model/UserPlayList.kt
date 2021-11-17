package com.example.musiqal.userPLaylists.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musiqal.datamodels.youtubeItemInList.Item

@Entity(tableName = "userPLayLists")
data class UserPlayList(
    var playListName: String,
    var playLisLastUpdate: String,
    var playListCoverUrl:String,
    val playListItems: List<Item>
)
{
    @PrimaryKey var playListId:Int?=null
}
