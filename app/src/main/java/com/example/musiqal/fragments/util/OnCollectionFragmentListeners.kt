package com.example.musiqal.fragments.util

import com.example.musiqal.datamodels.youtubeItemInList.Item

interface OnCollectionFragmentListeners {
    fun onPlayedTracksClick(tracks:List<Item>)
}