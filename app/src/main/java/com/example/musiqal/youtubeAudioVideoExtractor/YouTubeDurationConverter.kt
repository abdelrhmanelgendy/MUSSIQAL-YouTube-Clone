package com.example.musiqal.youtubeAudioVideoExtractor

import java.lang.StringBuilder

object YouTubeDurationConverter {

    private fun getHours(time: String): Int {
        return time.substring(time.indexOf("T") + 1, time.indexOf("H")).toInt()
    }

    private fun getMinutes(time: String): Int {
        return time.substring(time.indexOf("H") + 1, time.indexOf("M")).toInt()
    }

    private fun getMinutesWithNoHours(time: String): Int {
        return time.substring(time.indexOf("T") + 1, time.indexOf("M")).toInt()
    }

    private fun getMinutesWithNoHoursAndNoSeconds(time: String): Int {
        return time.substring(time.indexOf("T") + 1, time.indexOf("M")).toInt()
    }

    private fun getSecondsOnly(time: String): Int {
        return time.substring(time.indexOf("T") + 1, time.indexOf("S")).toInt()
    }

    private fun getSecondsWithMinutes(time: String): Int {
        return time.substring(time.indexOf("M") + 1, time.indexOf("S")).toInt()
    }

    private fun getSecondsWithHours(time: String): Int {
        return time.substring(time.indexOf("H") + 1, time.indexOf("S")).toInt()
    }

    private fun convertToFormatedTime(time: String): FormatedTime {

        val HOURS_CONDITION = time.contains("H")
        val MINUTES_CONDITION = time.contains("M")
        val SECONDS_CONDITION = time.contains("S")

        /**
         *potential cases
         *hours only
         *minutes only
         *seconds only
         *hours and minutes
         *hours and seconds
         *hours and minutes and seconds
         *minutes and seconds
         */
        val formatTime = FormatedTime(-1, -1, -1)

        if (time.equals("P0D")) {
            //Live Video
            return formatTime
        } else {
            var hours = -1
            var minutes = -1
            var seconds = -1

            if (HOURS_CONDITION && !MINUTES_CONDITION && !SECONDS_CONDITION) {
                //hours only
                hours = getHours(time)
            } else if (!HOURS_CONDITION && MINUTES_CONDITION && !SECONDS_CONDITION) {
                //minutes only
                minutes = getMinutesWithNoHoursAndNoSeconds(time)

            } else if (!HOURS_CONDITION && !MINUTES_CONDITION && SECONDS_CONDITION) {
                //seconds only
                seconds = getSecondsOnly(time)


            } else if (HOURS_CONDITION && MINUTES_CONDITION && !SECONDS_CONDITION) {
                //hours and minutes
                hours = getHours(time)
                minutes = getMinutes(time)

            } else if (HOURS_CONDITION && !MINUTES_CONDITION && SECONDS_CONDITION) {
                //hours and seconds
                hours = getHours(time)
                seconds = getSecondsWithHours(time)


            } else if (HOURS_CONDITION && MINUTES_CONDITION && SECONDS_CONDITION) {
                //hours and minutes and seconds
                hours = getHours(time)
                minutes = getMinutes(time)
                seconds = getSecondsWithMinutes(time)


            } else if (!HOURS_CONDITION && MINUTES_CONDITION && SECONDS_CONDITION) {
                //minutes and seconds
                minutes = getMinutesWithNoHours(time)
                seconds = getSecondsWithMinutes(time)

            }
            return FormatedTime(hours, minutes, seconds)
        }
    }

    fun getTimeInStringFormated(time: String): String {
        //PT2M51S -> 00:02:51  PT3M->00:03:00   PT11H54M48S->11:54:48
        val formatedTime = convertToFormatedTime(time)
        val timeFormate: StringBuilder = StringBuilder("")

        if (formatedTime.hour == -1) {
            timeFormate.append("00:")
        } else if (formatedTime.hour.toString().length == 1) {
            timeFormate.append("0" + (formatedTime.hour).toString() + ":")
        } else {
            timeFormate.append((formatedTime.hour).toString() + ":")
        }
        if (formatedTime.minutes == -1) {
            timeFormate.append("00:")
        } else if (formatedTime.minutes.toString().length == 1) {
            timeFormate.append("0" + (formatedTime.minutes).toString() + ":")
        } else {
            timeFormate.append((formatedTime.minutes).toString() + ":")
        }

        if (formatedTime.second == -1) {
            timeFormate.append("00")
        } else if (formatedTime.second.toString().length == 1) {
            timeFormate.append("0" + (formatedTime.second).toString())
        } else {
            timeFormate.append(formatedTime.second)
        }
        return timeFormate.toString()
    }

    fun getTimeInSeconds(time: String): Int {
//        PT11H54M48S->   42888
        val formatedTime = convertToFormatedTime(time)
        var tottalTimeInSeconds = 0
        if (formatedTime.hour != -1) {
            tottalTimeInSeconds += (formatedTime.hour * 60 * 60)
        }
        if (formatedTime.minutes != -1) {
            tottalTimeInSeconds += (formatedTime.minutes * 60)
        }
        if (formatedTime.second != -1) {
            tottalTimeInSeconds += (formatedTime.second)

        }
        return tottalTimeInSeconds
    }
    fun getTimeInMillis(time: String):Long
    {
        val timeInSeconds = getTimeInSeconds(time)
        return (timeInSeconds*1000).toLong()
    }

}