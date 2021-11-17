package com.example.musiqal.datamodels.youtubeApiSearchForVideo

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemConverters {
    var gson: Gson

    init {
        gson = Gson()
    }

    fun convertToItems(textedItem: String): List<Item> {
        val type = object : TypeToken<List<Item>>() {}.type
        return gson.fromJson(textedItem, type)
    }
    fun convertToTextedItems(items:List<Item>):String
    {
        return gson.toJson(items)
    }
}