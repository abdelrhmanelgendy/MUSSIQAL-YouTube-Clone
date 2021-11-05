package com.example.musiqal.util

import com.example.musiqal.models.youtubeApiSearchForVideo.Item


interface OnSearchedItemClickListner {
    fun onSearchResultClick(
        item:Item,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,
        )
}
