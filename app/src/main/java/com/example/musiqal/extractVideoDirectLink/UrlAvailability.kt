package com.example.musiqal.util

import android.util.Log
import java.lang.Exception
import java.net.URL

object UrlAvailability {
    fun checkUrl(url: String): ContentAvaialbility {
        try {
            val connectionUrl = URL(url)
            val openConnection = connectionUrl.openConnection()!!
            val contentLength = openConnection.contentLength
            val contentType = openConnection.contentType!!

            val availability= (contentLength > 0)
            return ContentAvaialbility(availability,contentLength,contentType)
        } catch (e: Exception) {
            return ContentAvaialbility(false,-1,"-1")
        }


    }
    fun checkAudioAvailability(url: String):Boolean
    {
        try {
            val connectionUrl = URL(url)
            val openConnection = connectionUrl.openConnection()!!
            val contentLength = openConnection.contentLength
            val contentType = openConnection.contentType!!
            Log.d("filterResults", "checkAudioAvailability: "+contentLength+"   "+contentType)

            return contentLength > 0 && contentType.toLowerCase().contains("audio")
        } catch (e: Exception) {
           return false
        }

    }
}
data class ContentAvaialbility(val isAvailable:Boolean,val length:Int,val contentType:String)