package com.example.musiqal.datamodels

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringConverter {
    @TypeConverter
    fun convertToString(list: List<String>)
    =Gson().toJson(list)

    @TypeConverter
    fun convertToList(list: String):List<String>{
        val type=object :TypeToken<List<String>>(){}.type!!
        return Gson().fromJson(list,type)
    }

}