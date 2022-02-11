package com.example.musiqal.customeMusicPlayer.util

import com.example.musiqal.datamodels.youtubeItemInList.Item

public interface MusicPlayerEvents {
    fun start(
        url: String,
        currentItem: Item,
        currentPlayList: List<Item>,
        currentItemPosition: Int
    ,playListName:String,
        currentDuration:Long=-1

    )

    fun pause()
    fun resume()
    fun stop()
    fun next(url: String){}
    fun next(url: String,currentItem: Item){}
    fun previouse(url: String)

    fun addFavorite()
    fun seekTo(newTime: Int)


}