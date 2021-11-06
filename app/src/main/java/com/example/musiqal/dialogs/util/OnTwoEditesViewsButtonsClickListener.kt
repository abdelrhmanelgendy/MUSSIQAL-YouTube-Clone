package com.example.musiqal.dialogs.util

import com.example.musiqal.lyrics.model.SongAndSingerName

interface OnTwoEditesViewsButtonsClickListener {
    fun onPositiveButtonClick(songAndSingerName: SongAndSingerName)
    fun onNegativeButtonClick(songAndSingerName: SongAndSingerName)
}
