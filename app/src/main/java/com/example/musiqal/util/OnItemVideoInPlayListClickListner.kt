package com.example.musiqal.util

import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.datamodels.youtubeItemInList.ItemInPlayListPreview

interface OnItemVideoInPlayListClickListner {
    fun onVideoClick(
        item: Item,
        idsList: List<String>,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,
        _listOfSelectableYoutubeItemsInPLayListPreview: MutableList<ItemInPlayListPreview>
    ){}
    fun onVideoClick(
        item: Item,
        idsList: List<String>,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int

    ){}
}