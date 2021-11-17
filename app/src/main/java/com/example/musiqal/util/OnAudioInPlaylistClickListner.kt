package com.example.musiqal.util

import com.example.musiqal.customeMusicPlayer.util.ShuffleMode
import com.example.musiqal.datamodels.youtubeItemInList.Item

interface OnAudioInPlaylistClickListner {
    fun onItemClick(
        item: Item,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,
        playListName: String
    )

    fun onItemClick(
        item: Item,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,
        playListName: String,
        shuffleMode: ShuffleMode
    ){}

}