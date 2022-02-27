package com.example.musiqal.database

import android.content.Context

class TracksImageSharedPref(private val context: Context) {
    private val FILE_NAME = "images_files"
    private val IMAGE_KEY = "image"
    fun saveImageWithId(id: Long, imgUrl: String) {
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(id.toString(),imgUrl).apply()

    }

    fun getImageUrlByTaskId(taskId: Long): String {
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
       return sharedPreferences.getString(taskId.toString(),"-1")!!
    }

}