package com.example.musiqal.util

import com.example.musiqal.models.youtube.converter.toaudio.YoutubeMp3ConverterData
import com.example.musiqal.models.youtubeItemInList.Item

interface OnAudioInPlaylistClickListner {
    fun onItemClick(item: Item, _listOfYoutubeItemsInPlaylists: MutableList<Item>,position:Int,playListName: String)

}