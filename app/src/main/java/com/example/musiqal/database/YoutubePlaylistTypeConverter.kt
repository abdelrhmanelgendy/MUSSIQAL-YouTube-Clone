package com.example.musiqal.database

import androidx.room.TypeConverter
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.Id
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.Item
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.Snippet
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.Thumbnails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class YoutubePlaylistTypeConverter {


    @TypeConverter
    fun convertSnippetToString(snippet: Snippet): String {
        return Gson().toJson(snippet)
    }

    @TypeConverter
    fun convertToSnippet(snippet: String): Snippet {
        return Gson().fromJson(snippet, Snippet::class.java)
    }

    @TypeConverter
    fun convertThumbnailsToString(thumbnails: Thumbnails): String {
        return Gson().toJson(thumbnails)
    }

    @TypeConverter
    fun convertToThumbnails(thumbnails: String): Thumbnails {
        return Gson().fromJson(thumbnails, Thumbnails::class.java)
    }

    @TypeConverter
    fun convertToIdString(id: Id): String {
        return Gson().toJson(id)
    }

    @TypeConverter
    fun convertStringToId(id: String): Id {
        return Gson().fromJson(id, Id::class.java)
    }

    @TypeConverter
    fun convertToListItemString(listOfItems: List<Item>): String {
        return Gson().toJson(listOfItems)
    }

    @TypeConverter
    fun convertStringToListItem(items: String): List<Item> {
        val type: Type = object : TypeToken<List<Item>>() {}.type
        return Gson().fromJson(items,type)
    }


}