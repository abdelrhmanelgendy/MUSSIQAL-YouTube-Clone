package com.example.musiqal.customeMusicPlayer.util

import android.media.MediaPlayer

interface OnCustomeMusicPlayerCompletionListener {
fun onComplete(mediaPlayer: MediaPlayer)
fun onSeeking(seekedDurationInMilles:Int)
}
