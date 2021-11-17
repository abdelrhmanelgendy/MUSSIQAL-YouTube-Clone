package com.example.musiqal.datamodels.youtubeItemInList

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemTypeConverter {
    @TypeConverter
    fun convertSnippetToString(snippet: Snippet): String {
        return Gson().toJson(snippet)
    }
    @TypeConverter
    fun convertToSnippet(snippet: String): Snippet {
        return Gson().fromJson(snippet, Snippet::class.java)
    }
    @TypeConverter
    fun convertResourceIdToString(resourceId: ResourceId): String {
        return Gson().toJson(resourceId)
    }
    @TypeConverter
    fun convertToSResourceId(resourceId: String): ResourceId {
        return Gson().fromJson(resourceId, ResourceId::class.java)
    }
    @TypeConverter
    fun convertThunbnailsToString(thumbnails: Thumbnails): String {
        return Gson().toJson(thumbnails)
    }
    @TypeConverter
    fun convertToThunbnails(thumbnails: String): Thumbnails {
        return Gson().fromJson(thumbnails, Thumbnails::class.java)
    }
    @TypeConverter
    fun convertDefaultThunbnailsToString(default: Default): String {
        return Gson().toJson(default)
    }

    @TypeConverter
    fun convertToDefaultThunbnails(thumbnails: String): Default {
        return Gson().fromJson(thumbnails, Default::class.java)
    }

    @TypeConverter
    fun convertHighThunbnailsToString(high: High): String {
        return Gson().toJson(high)
    }
    @TypeConverter
    fun convertToHighThunbnails(thumbnails: String): High {
        return Gson().fromJson(thumbnails, High::class.java)
    }

    @TypeConverter
    fun convertMaxresThunbnailsToString(default: Maxres): String {
        return Gson().toJson(default)
    }
    @TypeConverter
    fun convertToMaxresThunbnails(thumbnails: String): Maxres {
        return Gson().fromJson(thumbnails, Maxres::class.java)
    }

    @TypeConverter
    fun convertMediumThunbnailsToString(default: Medium): String {
        return Gson().toJson(default)
    }
    @TypeConverter
    fun convertToMediumThunbnails(thumbnails: String): Medium {
        return Gson().fromJson(thumbnails, Medium::class.java)
    }
    @TypeConverter
    fun convertStandardThunbnailsToString(default: Standard): String {
        return Gson().toJson(default)
    }
    @TypeConverter
    fun convertToStandardThunbnails(thumbnails: String): Standard {
        return Gson().fromJson(thumbnails, Standard::class.java)
    }

    @TypeConverter
    fun convertStandardListOfItemsString(listOfItems: List<Item>): String {
        return Gson().toJson(listOfItems.toString())
    }
    @TypeConverter
    fun convertToListOfItems(listOfItems: String): List<Item> {
        val type=object :TypeToken<List<Item>>(){}.type
        return Gson().fromJson(listOfItems,type)
    }



}