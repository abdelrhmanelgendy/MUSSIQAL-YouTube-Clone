package com.example.musiqal.customeMusicPlayer.util

sealed class ShuffleMode(val shuffleMode:String){
    object Shuffle: ShuffleMode("Shuffle")
    object NoShuffle: ShuffleMode("No-Shuffle")

}