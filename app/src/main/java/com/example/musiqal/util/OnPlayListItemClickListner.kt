package com.example.musiqal.util

import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.Item
 interface OnPlayListItemClickListner {
    fun onItemClick(item: Item)
}