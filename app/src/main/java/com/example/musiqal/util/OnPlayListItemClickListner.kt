package com.example.musiqal.util

import com.example.musiqal.models.youtubeApiSearchForPlayList.Item

interface OnPlayListItemClickListner {
    fun onItemClick(item: Item)
}