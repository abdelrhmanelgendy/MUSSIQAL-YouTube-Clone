package com.example.musiqal.customeMusicPlayer.util

data class LastPlayedTrack(val currentDuration:Long,val trackId:String)
{
    companion object
    {
        fun dummyTrack()=LastPlayedTrack(-1,"-9")
    }
}
