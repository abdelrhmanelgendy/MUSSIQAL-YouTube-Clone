package com.example.musiqal.lyrics

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.VolleyError
import com.example.musiqal.dialogs.SimpleYesOrNoDialog
import com.example.musiqal.lyrics.util.YoutubeLyricsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LyricsUtil(private val context: Context) {
    private val TAG = "LyricsUtil"
    private var simpleYesOrNoDialog: SimpleYesOrNoDialog
    private val gettingSongMainTitle = "Song Lyrics From Youtube"
    private var youtubeSubtitleLyrics: YoutubeSubtitleLyrics

    init {
        simpleYesOrNoDialog = SimpleYesOrNoDialog(context)
        youtubeSubtitleLyrics = YoutubeSubtitleLyrics(context)
    }

    fun initialize(videoId: String, songName: String) {
        if (videoId.length==0)
        {
            throw Throwable("Invalid id ")
        }
        showDialog(songName)
        youtubeSubtitleLyrics.getLyricsByVideoId(videoId, object : YoutubeLyricsListener {
            override fun onSuccess(lyrics: String) {
                if (lyrics.length==0)
                {
                    Toast.makeText(context,"Failed to get this way",Toast.LENGTH_SHORT).show()
                    dismissDialog()
                    return
                }
                dismissDialog()
                Log.d(TAG, "onSuccess: " + lyrics)

            }

            override fun onFailed(e: Throwable?) {
                Log.d(TAG, "onFailed: " + e?.message.toString())
                if (e?.message.toString().contains("Unable to resolve host \"video.google.com\": No address associated with hostname")!!)
                {

                    Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onFailed: no internet")
                }
                CoroutineScope(Dispatchers.Default).launch{
                  delay(100)
                    dismissDialog()
                }
            }
        })


    }

    private fun showDialog(songName: String) {
        simpleYesOrNoDialog.intialize(gettingSongMainTitle, songName, "", "", -1, false)
        simpleYesOrNoDialog.show(false)
    }

    private fun dismissDialog() {
        simpleYesOrNoDialog.dismis()
    }
}