package com.example.musiqal.datamodels.youtubeItemInList

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class YoutubeItemsConverters {
    fun convertToString(items: List<Item>) = Gson().toJson(items)
    fun convertItems(items: String): List<Item> {
        val type = object : TypeToken<List<Item>>() {}.type
        return Gson().fromJson(items, type)
    }


}