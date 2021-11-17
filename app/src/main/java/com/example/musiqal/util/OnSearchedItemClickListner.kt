package com.example.musiqal.util

import com.example.musiqal.datamodels.youtubeApiSearchForVideo.Item


interface OnSearchedItemClickListner {
    fun onSearchResultClick(
        item:Item,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,
        )
}
