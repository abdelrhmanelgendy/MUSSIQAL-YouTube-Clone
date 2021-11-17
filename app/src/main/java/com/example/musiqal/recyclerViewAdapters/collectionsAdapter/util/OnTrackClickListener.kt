package com.example.musiqal.recyclerViewAdapters.collectionsAdapter.util

import com.example.musiqal.datamodels.youtubeItemInList.Item

interface OnTrackClickListener {
    fun onVideoClick(
        item: Item,
        idsList: List<String>,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,

    )
}