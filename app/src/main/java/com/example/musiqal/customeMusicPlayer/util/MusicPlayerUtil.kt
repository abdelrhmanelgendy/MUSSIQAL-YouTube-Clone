package com.example.musiqal.customeMusicPlayer.util

import android.net.Uri
import android.util.Log
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.math.min

object MusicPlayerUtil {
    fun formatTime(tottalTime: Int): String {
//         tottalTime-> Seconds

        try {
            val timeBuilder = StringBuilder()
            if (tottalTime <= 60) {
                Log.d("TAG", "formatTime: less than minute")
                val seconds = tottalTime
                if (seconds.toString().length == 1) {
                    timeBuilder.append("00:0${seconds}")
                } else {
                    timeBuilder.append("00:${seconds}")
                }


            } else if (tottalTime > 60 && tottalTime <= (60 * 60)) {
                Log.d("TAG", "formatTime: less than hour")
                val seconds = (tottalTime % 60).toString()
                val minutes = (tottalTime / 60).toString()
                if (seconds.length == 1) {
                    timeBuilder.append("$minutes:0$seconds")
                } else {
                    timeBuilder.append("$minutes:$seconds")
                }
            } else if (tottalTime > (60 * 60)) {
                Log.d("TAG", "formatTime: more than hour")
                val minutes = tottalTime % (60 * 60) / 60
                val hours = tottalTime / (60 * 60)
                val seconds = tottalTime % (60 * 60) % 60
                var hourString = hours.toString()
                var minutesString = minutes.toString()
                var secondsString = seconds.toString()
                if (minutes.toString().length == 1) {
                    minutesString = "0" + minutesString
                }
                if (seconds.toString().length == 1) {
                    secondsString = "0" + secondsString
                }
                if (hours.toString().length == 1) {
                    hourString = "0" + hourString
                }
                timeBuilder.append("$hourString:$minutesString:$secondsString")
            }
            return timeBuilder.toString()
        } catch (e: Exception)
        {
            return (tottalTime/1000).toString()
        }


    }

    fun convertVideoUrlToUri(url: String): Uri {
        return Uri.parse(url)
    }
}