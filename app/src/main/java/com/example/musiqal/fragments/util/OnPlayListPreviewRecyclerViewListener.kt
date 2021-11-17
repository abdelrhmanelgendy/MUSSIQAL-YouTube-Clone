package com.example.musiqal.fragments.util

import com.example.musiqal.datamodels.youtubeItemInList.Item

interface OnPlayListPreviewRecyclerViewListener {
    fun onScroll(position: Int, currentListOfItems: List<Item>)
}