package com.example.musiqal.customeMusicPlayer

import android.media.MediaPlayer

interface OnCustomeMusicPlayerCompletionListener {
fun onComplete(mediaPlayer: MediaPlayer)
fun onSeeking(seekedDurationInMilles:Int)
}
