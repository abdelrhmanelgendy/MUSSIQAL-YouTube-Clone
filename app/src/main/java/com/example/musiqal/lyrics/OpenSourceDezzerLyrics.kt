package com.example.musiqal.lyrics

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.musiqal.dialogs.TwoEditeTextDialog
import com.example.musiqal.dialogs.util.OnTwoEditesViewsButtonsClickListener
import com.example.musiqal.lyrics.model.SongAndSingerName
import com.example.musiqal.lyrics.mvi.LyricsViewModel
import com.example.musiqal.lyrics.mvi.LyricsViewState
import com.example.musiqal.lyrics.util.OnDezzerLyricsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

class OpenSourceDezzerLyrics(
    val context: Context,
    val onDezzerLyricsListener: OnDezzerLyricsListener
) {
    private var twoEditeTextDialog: TwoEditeTextDialog
    private val TAG = "OpenSourceDezzerLyrics"

    init {
        twoEditeTextDialog = TwoEditeTextDialog(context)
    }

    val lyricsViewModel: LyricsViewModel by lazy {
        ViewModelProvider(context as AppCompatActivity).get(LyricsViewModel::class.java)
    }
    var isDataFailuer = false
    fun trySearchBySongDataWithoutUser(tottalSongName: String) {
        isDataFailuer = true
        Log.d(TAG, "trySearchBySongDataWithoutUser: 2332")
        val songAndSingerName = SongPrediction().predictSingerAndSong(tottalSongName)
        lyricsViewModel.searchForSongLyrics(
            songAndSingerName.singerName,
            songAndSingerName.songName
        )

        CoroutineScope(Dispatchers.Main)
            .launch {
                lyricsViewModel.songLyricsStateFlow.collectIndexed { index, it ->
                    when (it) {
                        is LyricsViewState.Loading -> {
                        }
                        is LyricsViewState.Success -> {
                            Log.d(TAG, "onPositiveButtonClick: 1" + it.lyrics.lyrics)
                            onDezzerLyricsListener.onSuccess(it.lyrics.lyrics)
                            twoEditeTextDialog.dismis()
                        }
                        is LyricsViewState.Failed -> {

                           if (index==1)
                           {
                               if (isDataFailuer) {
                                   Log.d(TAG, "trySearchBySongDataWithoutUser:  failed1")
                                   Toast.makeText(
                                       context,
                                       "Failed to automatically get lyrics,\n" +
                                               "search it yourself",
                                       Toast.LENGTH_SHORT
                                   ).show()
                                   trySearchBySongDataWithUserHelp(songAndSingerName)
                                   isDataFailuer = false
                               }
                           }

                        }

                    }
                }
            }
    }

    var withUserHelp = true
    var isUserClicked=false
    private fun trySearchBySongDataWithUserHelp(songAndSingerName: SongAndSingerName) {

        if (withUserHelp) {
            withUserHelp = false
            Log.d(TAG, "trySearchBySongDataWithUserHelp: ")
            twoEditeTextDialog.intialize(
                songAndSingerName.singerName,
                songAndSingerName.songName,
                "Search",
                "Cancel",
                object :
                    OnTwoEditesViewsButtonsClickListener {
                    override fun onPositiveButtonClick(songAndSingerName: SongAndSingerName) {
                        isUserClicked=true
                        hideInteractionButtons()
                        Log.d(TAG, "onPositiveButtonClick: 2" + songAndSingerName.songName)
                        lyricsViewModel.searchForSongLyrics(
                            songAndSingerName.singerName,
                            songAndSingerName.songName
                        )

                        CoroutineScope(Dispatchers.Main)
                            .launch {
                                lyricsViewModel.songLyricsStateFlow.collect {
                                    when (it) {
                                        is LyricsViewState.Loading -> {
                                        }
                                        is LyricsViewState.Success -> {
                                            showInteractionButtons()
                                            Log.d(
                                                TAG,
                                                "onPositiveButtonClick: 1" + it.lyrics.lyrics
                                            )
                                            onDezzerLyricsListener.onSuccess(it.lyrics.lyrics)
                                            twoEditeTextDialog.dismis()
                                        }
                                        is LyricsViewState.Failed -> {
                                           if (isUserClicked)
                                           {
                                               showInteractionButtons()
                                               Log.d(
                                                   TAG,
                                                   "onPositiveButtonClick: failed " + it.errorMessgae
                                               )
                                               onDezzerLyricsListener.onFailed(it.errorMessgae)
                                               isUserClicked=false
                                           }
                                        }

                                    }
                                }
                            }


                    }

                    override fun onNegativeButtonClick(songAndSingerName: SongAndSingerName) {
                        Log.d(TAG, "onPositiveButtonClick: " + songAndSingerName.singerName)
                        twoEditeTextDialog.dismis()
                    }
                })
            twoEditeTextDialog.show(false)

        }


    }

    private fun hideInteractionButtons() {
        twoEditeTextDialog.hideButtons(false)
    }

    private fun showInteractionButtons() {
        twoEditeTextDialog.hideButtons(true)
    }
}