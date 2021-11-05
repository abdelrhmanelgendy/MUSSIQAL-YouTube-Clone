package com.example.musiqal.customeMusicPlayer.util

import com.example.musiqal.models.youtubeItemInList.Item

public interface MusicPlayerEvents {
    fun start(
        url: String,
        currentItem: Item,
        currentPlayList: List<Item>,
        currentItemPosition: Int
    ,playListName:String

    )

    fun pause()
    fun resume()
    fun stop()
    fun next(url: String)
    fun previouse(url: String)

    fun addFavorite()
    fun seekTo(newTime: Int)


}