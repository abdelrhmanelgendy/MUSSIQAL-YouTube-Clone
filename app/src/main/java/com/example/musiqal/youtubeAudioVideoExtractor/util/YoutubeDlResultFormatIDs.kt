package com.example.musiqal.youtubeAudioVideoExtractor.util

object YoutubeDlResultFormatIDs {
    val VIDEO_LIST= listOf(5,6,17,18,22,34,35,36,37,38,43,44,45,46,82,83,84,85,92,93,94,95,96,100,101,102,132,151,394,395,396,397,398,399,400,401,402,272)
    val AUDIO_LIST= listOf(139,140,141,171,249,250,251)
    sealed class IsVideoState(var msg:String)
    {
        object VIDEO:IsVideoState("video")
        object AUDIO:IsVideoState("audio")
        object UNKNOWN:IsVideoState("unkown")
    }

}