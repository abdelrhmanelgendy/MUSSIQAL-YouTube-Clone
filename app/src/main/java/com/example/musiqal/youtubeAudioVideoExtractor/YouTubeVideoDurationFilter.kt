package com.example.musiqal.youtubeAudioVideoExtractor

//package com.example.musiqal.util
//
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import com.example.musiqal.models.youtubeItemInList.Item
//import java.lang.Exception
//import java.text.SimpleDateFormat
//import java.time.Duration
//import java.time.LocalTime
//import java.time.Period
//import java.util.*
//
//object YouTubeVideoDurationFilter {
//    val formatedTime= com.example.musiqal.youtubeAudioVideoExtractor.FormatedTime(-1,-1,-1)
//    fun gettingTimeInSeconds(item: Item): Long {
//
//
//        val videoDuration = item.videoDuration
//        val timeFormatedFromDuration = getTimeFromString(videoDuration)!!
//        Log.d("TAG", "gettingTimeInSeconds: "+timeFormatedFromDuration)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return convertTimeToSecondsApiM(timeFormatedFromDuration)
//        } else {
//            return convertTimeToSeconds(timeFormatedFromDuration)
//        }
//
//
//    }
//
//    fun gettingTimeInSeconds(videoDuration: String): Long {
//
//
//
//        val timeFormatedFromDuration = getTimeFromString(videoDuration)!!
//        Log.d("TAG", "gettingTimeInSeconds: "+timeFormatedFromDuration)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return convertTimeToSecondsApiM(timeFormatedFromDuration)
//        } else {
//            return convertTimeToSeconds(timeFormatedFromDuration)
//        }
//
//
//    }
//
//    private fun convertTimeToSeconds(timeFormatedFromDuration: String): Long {
//        var timeInSeconds = timeFormatedFromDuration
//        if (timeInSeconds.length == 5) {
//            timeInSeconds = "00:" + timeInSeconds
//        }
//        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
//        val parse = simpleDateFormat.parse(timeInSeconds)
//        var tottalSeconds = 0L
//        val houres = parse?.hours!!
//        val minutes = parse.minutes
//        val seconds = parse.seconds
//        formatedTime.hour=houres
//        formatedTime.minutes=minutes
//        formatedTime.second=seconds
//
//        if (houres != 0) {
//            tottalSeconds += (houres * 60*60)
//        }
//        if (minutes != 0) {
//            tottalSeconds += (minutes * 60)
//        }
//        if (seconds != 0) {
//            tottalSeconds += seconds
//        }
//        return tottalSeconds
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun convertTimeToSecondsApiM(timeFormatedFromDuration: String): Long {
////        try {
//            Log.d("TAG", "convertTimeToSecondsApiM: $timeFormatedFromDuration ${timeFormatedFromDuration.length}")
//            if (timeFormatedFromDuration.equals("00:"))
//            {
//                return 410
//            }
//            var timeInSeconds = timeFormatedFromDuration
//            if (timeInSeconds.length == 5) {
//                timeInSeconds = "00:" + timeInSeconds
//            }
//        //11:55:
//        Log.d("TAG,", "convertTimeToSecondsApiM: "+timeInSeconds.length+"  "+timeInSeconds.get(5))
//        if (timeInSeconds.length==6&&timeInSeconds.get(5).toString().equals(":"))
//        {
//            timeInSeconds=timeInSeconds+"00"
//        }
//
//            val period = LocalTime.parse(timeInSeconds)!!
//
//            var tottalSeconds = 0L
//            val houres = period.hour
//            val minutes = period.minute
//            val seconds = period.second
//            formatedTime.hour=houres
//            formatedTime.minutes=minutes
//            formatedTime.second=seconds
//            if (houres != 0) {
//                tottalSeconds += (houres * 60*60)
//            }
//            if (minutes != 0) {
//                tottalSeconds += (minutes * 60)
//            }
//            if (seconds != 0) {
//                tottalSeconds += seconds
//            }
//            return tottalSeconds
////        }
////        catch (e:Exception)
////        {
////            return 410
//
//    }
//
//
//    private fun getTimeFromString(duration: String): String? {
//        var time: String? = ""
//        var hourexists = false
//        var minutesexists = false
//        var secondsexists = false
//        if (duration.contains("H")) hourexists = true
//        if (duration.contains("M")) minutesexists = true
//        if (duration.contains("S")) secondsexists = true
//        if (hourexists) {
//            var hour = ""
//            hour = duration.substring(
//                duration.indexOf("T") + 1,
//                duration.indexOf("H")
//            )
//            if (hour.length == 1) hour = "0$hour"
//            time += "$hour:"
//        }
//        if (minutesexists) {
//            var minutes = ""
//            minutes = if (hourexists) duration.substring(
//                duration.indexOf("H") + 1,
//                duration.indexOf("M")
//            ) else duration.substring(
//                duration.indexOf("T") + 1,
//                duration.indexOf("M")
//            )
//            if (minutes.length == 1) minutes = "0$minutes"
//            time += "$minutes:"
//        } else {
//            time += "00:"
//        }
//        if (secondsexists) {
//            var seconds = ""
//            seconds = if (hourexists) {
//                if (minutesexists) duration.substring(
//                    duration.indexOf("M") + 1,
//                    duration.indexOf("S")
//                ) else duration.substring(
//                    duration.indexOf("H") + 1,
//                    duration.indexOf("S")
//                )
//            } else if (minutesexists) duration.substring(
//                duration.indexOf("M") + 1,
//                duration.indexOf("S")
//            ) else duration.substring(
//                duration.indexOf("T") + 1,
//                duration.indexOf("S")
//            )
//            if (seconds.length == 1) seconds = "0$seconds"
//            time += seconds
//        }
//        return time
//    } }
data class FormatedTime(var hour:Int,var minutes:Int,var second:Int)