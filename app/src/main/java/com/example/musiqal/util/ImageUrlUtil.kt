package com.example.musiqal.util

import com.example.musiqal.datamodels.youtubeItemInList.Item

object ImageUrlUtil
{
    fun getMiniResolutionImageUrl(item: Item):String
    {
        if (item.snippet.thumbnails.default != null) {
            return item.snippet.thumbnails.default.url
        }
        if (item.snippet.thumbnails.medium != null) {
            return item.snippet.thumbnails.medium.url
        }
        if (item.snippet.thumbnails.high != null) {
            return item.snippet.thumbnails.high.url
        }
        if (item.snippet.thumbnails.standard != null) {
            return item.snippet.thumbnails.standard.url
        }
        if (item.snippet.thumbnails.maxres != null) {
            return item.snippet.thumbnails.maxres.url
        }
        return ""
    }
    fun getMaxResolutionImageUrl(item:Item):String
    {
        if (item.snippet.thumbnails.maxres != null) {
            return item.snippet.thumbnails.maxres.url
        }
        if (item.snippet.thumbnails.standard != null) {
            return item.snippet.thumbnails.standard.url
        }
        if (item.snippet.thumbnails.high != null) {
            return item.snippet.thumbnails.high.url
        }
        if (item.snippet.thumbnails.medium != null) {
            return item.snippet.thumbnails.medium.url
        }
        if (item.snippet.thumbnails.default != null) {
            return item.snippet.thumbnails.default.url
        }
        return ""
    }

    fun getMeduimResolutionImageUrl(item:Item):String
    {
        if (item.snippet.thumbnails.high != null) {
            return item.snippet.thumbnails.high.url
        }
        if (item.snippet.thumbnails.medium != null) {
            return item.snippet.thumbnails.medium.url
        }
        if (item.snippet.thumbnails.default != null) {
            return item.snippet.thumbnails.default.url
        }

        return ""
    }


}
