package com.example.musiqal.lyrics.lyricsdatabase

import android.content.Context
import android.util.Log
import com.example.musiqal.lyrics.model.SharedPrefLyricsLocalDataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LyricsSharedPreferences(private val context: Context) {
    companion object
    {
        public val LYRICS_FILE_NAME ="file_name"
        public val LYRICS_NAME ="lyrics"
    }
    public fun save(sharedPrefLyricsLocalDataModel: SharedPrefLyricsLocalDataModel)
    {
        val newList = mutableListOf<SharedPrefLyricsLocalDataModel>()
        val savedList = getSaved()
        if (savedList.size==0)
        {

            newList.add(sharedPrefLyricsLocalDataModel)
        }
        Log.d("getSaved", "save: "+savedList.toString())
        val sharedPreferences = context.getSharedPreferences(LYRICS_FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit().putString(LYRICS_NAME,convertListOfModelToString(newList)).apply()

    }
    public fun getSaved():List<SharedPrefLyricsLocalDataModel>
    {
        val sharedPreferences = context.getSharedPreferences(LYRICS_FILE_NAME, Context.MODE_PRIVATE)
        val data = sharedPreferences.getString(LYRICS_NAME, "-1")
        Log.d("TAG", "getSaved: "+data)
        if (data!=null&&data!="-1")
        {

            return convertStringToListOfModel(data)
        }
        return listOf()
    }

    private fun convertListOfModelToString(list:List<SharedPrefLyricsLocalDataModel>):String
    {
        return Gson().toJson(list)
    }
    private fun convertStringToListOfModel(data:String):List<SharedPrefLyricsLocalDataModel>
    {
        val type = object : TypeToken<List<SharedPrefLyricsLocalDataModel>>(){}.type
        return Gson().fromJson(data,type)
    }
}