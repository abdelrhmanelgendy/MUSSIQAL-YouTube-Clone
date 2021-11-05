package com.example.musiqal.customeMusicPlayer

import android.net.Uri

object MusicPlayerUtil {
     fun formatTime(tottalTime: Int): String {
        var tottalOut = ""
        var tottalNew = ""
        val seconds = (tottalTime % 60).toString()
        val minutes = (tottalTime / 60).toString()
        tottalOut = minutes + ":" + seconds
        tottalNew = minutes + ":" + "0" + seconds
        if (seconds.length == 1) {
            return tottalNew
        } else {
            return tottalOut
        }

    }

     fun convertVideoUrlToUri(url: String): Uri {
        return Uri.parse(url)
    }
}