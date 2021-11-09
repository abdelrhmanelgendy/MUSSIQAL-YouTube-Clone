package com.example.musiqal.recyclerViewAdapters.collectionsAdapter.util

import com.example.musiqal.models.youtubeItemInList.Item
import com.example.musiqal.models.youtubeItemInList.ItemInPlayListPreview

interface OnTrackClickListener {
    fun onVideoClick(
        item: Item,
        idsList: List<String>,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,

    )
}