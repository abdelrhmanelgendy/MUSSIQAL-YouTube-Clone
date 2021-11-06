package com.example.musiqal.lyrics

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.musiqal.lyrics.util.YoutubeLyricsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class YoutubeSubtitleLyrics(private val context: Context) {
    private val TAG = "YoutubeSubtitleLyrics11"
   private var newRequestQueue:RequestQueue
    init {

         newRequestQueue = Volley.newRequestQueue(context)

    }

    fun getLyricsByVideoId(videoId: String, youtubeLyricsListener: YoutubeLyricsListener) {
        val baseUrlMergedWithSongId = mergeUrl(videoId)
        kotlin.runCatching {
           CoroutineScope(Dispatchers.IO)
               .launch {
                   val stringRequest = StringRequest(Request.Method.GET, baseUrlMergedWithSongId,
                       { response ->
                           val lyricsFiltered = filterGoogleLyrics(response!!)
                           youtubeLyricsListener.onSuccess(lyricsFiltered)
                       },
                       { error ->
                           Log.d(TAG, "getLyricsByVideoId: " + error)
                           youtubeLyricsListener.onFailed(error?.cause)  }
                   )

                   newRequestQueue.add(stringRequest)
               }

        }.onFailure {
            youtubeLyricsListener.onFailed(it)
        }


    }

    private fun filterGoogleLyrics(response: String): String {
        val result =filterMainEncoding(response)

        val split = result.split("\">")
        var lyricsText = StringBuilder()
        for (i in 1..split.size - 1) {
            lyricsText.append(split[i].split("</").get(0) + "\n")
        }
        return lyricsText.toString()

    }

    private fun filterMainEncoding(response: String): String {
       return response
            .replace("<?xml version=\"1.0\" encoding=\"utf-8\" ?><transcript>", "")
            .replace("</transcript>", "")

    }

    private fun mergeUrl(videoId: String): String {
        val baseUrl = "https://video.google.com/timedtext?lang=en&v="
        return baseUrl + videoId
    }
}