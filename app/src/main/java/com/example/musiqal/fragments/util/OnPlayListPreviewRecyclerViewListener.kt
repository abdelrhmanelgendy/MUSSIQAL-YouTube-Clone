package com.example.musiqal.fragments.util

import com.example.musiqal.models.youtubeItemInList.Item

interface OnPlayListPreviewRecyclerViewListener {
    fun onScroll(position: Int, currentListOfItems: List<Item>)
}