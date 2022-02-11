package com.example.musiqal.fragments.library.adapter

import com.example.musiqal.lyrics.model.LyricsLocalDataModel
import com.example.musiqal.lyrics.model.SharedPrefLyricsLocalDataModel
import java.util.ArrayList

interface OnLyricsTrackClickListener {
    fun onTrackClick(
        lyricsLocalDataModel: SharedPrefLyricsLocalDataModel,
        position: Int,
        playedTracks: ArrayList<SharedPrefLyricsLocalDataModel>

    )
}