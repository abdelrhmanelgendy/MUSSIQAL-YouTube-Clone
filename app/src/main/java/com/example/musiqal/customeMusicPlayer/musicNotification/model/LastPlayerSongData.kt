package com.example.musiqal.customeMusicPlayer.musicNotification.model

import com.example.musiqal.datamodels.youtubeItemInList.*
import com.google.gson.Gson


data class LastPlayedSongData(val item: Item, val itemPosition: Int) {
    companion object {
        val item=Item(
        "-1", "-1", "-1", Snippet(
        "", "", "", "", -1, "",
        ResourceId("", ""), Thumbnails(
        Default(-1, "", -1), High(-1, "", -1), Maxres(-1, "", -1),
        Medium(-1, "", -1), Standard(-1, "", -1)
        ), "-1", "-1", "-1"
        ),"")
        fun provideEmptyPlayedSongData(): LastPlayedSongData {
            return LastPlayedSongData(
              item, -1
            )
        }
    }
}

object LastPlayerSongDataConverter {
    val gson = Gson()
    fun convertToLastPlayedSong(lastplayedData: String): LastPlayedSongData {
        return gson.fromJson(lastplayedData, LastPlayedSongData::class.java)
    }

    fun convertLastPlayedSongToString(lastPlayedSongData: LastPlayedSongData): String {
        return gson.toJson(lastPlayedSongData)
    }
}
