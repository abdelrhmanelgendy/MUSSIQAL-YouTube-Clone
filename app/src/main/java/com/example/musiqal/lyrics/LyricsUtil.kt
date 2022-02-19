package com.example.musiqal.lyrics

import android.content.Context
import android.util.Log
import com.example.musiqal.dialogs.SimpleYesOrNoDialog
import com.example.musiqal.lyrics.util.OnDezzerLyricsListener
import com.example.musiqal.lyrics.util.OnLyricsFoundListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LyricsUtil(private val context: Context, private val onLyricsFound: OnLyricsFoundListener) :
    OnDezzerLyricsListener {
    private val TAG = "LyricsUtil"
    private var simpleYesOrNoDialog: SimpleYesOrNoDialog
    private val gettingSongMainTitle = "Song Lyrics From Youtube"
    private var youtubeSubtitleLyrics: YoutubeSubtitleLyrics

    val Lyrics_CopyRight = "Paroles de la chanson"

    init {
        simpleYesOrNoDialog = SimpleYesOrNoDialog(context)
        youtubeSubtitleLyrics = YoutubeSubtitleLyrics(context)

    }

    fun initialize(videoId: String, songName: String) {
        Log.d(TAG, "openLyricsView: ")
        if (videoId.length == 0) {
            throw Throwable("Invalid id ")
        }
        showDialog(songName)
        getLyricsByOpenSourceLyricsApi(songName)
        CoroutineScope(Dispatchers.Default).launch {
            delay(100)
            dismissDialog()
        }
//        youtubeSubtitleLyrics.getLyricsByVideoId(videoId, object : YoutubeLyricsListener {
//            override fun onSuccess(lyrics: String) {
//                if (lyrics.length == 0) {
//                    dismissDialog()
//                    getLyricsByOpenSourceLyricsApi(songName)
//                    return
//                }
//                dismissDialog()
//                onLyricsFound.onSuccess(lyrics)
//
//            }
//
//            override fun onFailed(e: Throwable?) {
//                Log.d(TAG, "onFailed: " + e?.message.toString())
//                if (e?.message.toString()
//                        .contains("Unable to resolve host \"video.google.com\": No address associated with hostname")!!
//                ) {
//                    onLyricsFound.onFailed(e?.message!!)
//                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, "onFailed: no internet")
//                } else {
//                    getLyricsByOpenSourceLyricsApi(songName)
//                }
//                CoroutineScope(Dispatchers.Default).launch {
//                    delay(100)
//                    dismissDialog()
//                }
//            }
//        })


    }

    private fun showDialog(songName: String) {
        simpleYesOrNoDialog.intialize(gettingSongMainTitle, songName, "", "", -1, false)
        simpleYesOrNoDialog.show(false)
    }

    private fun dismissDialog() {
        simpleYesOrNoDialog.dismis()
    }

    fun getLyricsByOpenSourceLyricsApi(tottalSongName: String) {
        val openSourceDezzerLyrics = OpenSourceDezzerLyrics(context, this)
        openSourceDezzerLyrics.trySearchBySongDataWithoutUser(tottalSongName)

    }

    var isLyricsSuccessful = true
    override fun onSuccess(lyrics: String) {
//        if (isLyricsSuccessful) {
        val filteredLyrics = filterLyricsFromItsCopyRight(lyrics)

        onLyricsFound.onSuccess(lyrics)
        Log.d(TAG, "onSuccess:111")
        isLyricsSuccessful = false
//        }

    }

    private fun filterLyricsFromItsCopyRight(lyrics: String): String {
        val l1 = lyrics.replace(Lyrics_CopyRight, "")
        val l2 = l1.replace("par", "")
        return l2


    }

    override fun onFailed(error: String) {
        Log.d(TAG, "onFailed:11 " + error.toString())
        onLyricsFound.onFailed(error)
    }


}