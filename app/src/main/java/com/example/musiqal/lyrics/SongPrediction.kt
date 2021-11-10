package com.example.musiqal.lyrics

import android.util.Log
import com.example.musiqal.lyrics.model.SongAndSingerName
import java.util.*

class SongPrediction {
    private val TAG = "SongPredictionTG"
    val filterationWords = listOf<String>(
        "(Official Video)",
        "(Lyrics)",
        "(Lyric Video)",
        "(Audio)",
        "[Official Video]",
        "[Official Music Video]",
        "(Official Video)",
        "(Official Music Video)",
        "(Performance Edit)",
        "مترجمه",
        "مترجمة",

    )
    val dashesList= listOf("-","_","–")
    val featsList= listOf("ft","feat")

    public fun predictSingerAndSong(songName: String): SongAndSingerName {
        val filterSongNameFromRedundantWords = filterSongNameFromRedundantWords(songName)
        val ftFiltiration = filterSongFromFTWord(filterSongNameFromRedundantWords)
        val songAndSingerName= filterFromDashes(ftFiltiration)
        return songAndSingerName
    }

    private fun filterFromDashes(ftFiltiration: String): SongAndSingerName {
        for (s in dashesList)
        {
            if (ftFiltiration.contains(s))
            {
                val replacedSong = ftFiltiration.split(s)
                val singerName=replacedSong[0]
                val songName=replacedSong[1]


                return SongAndSingerName(singerName,songName)
            }
        }
        return SongAndSingerName(ftFiltiration,"")

    }

    private fun filterSongFromFTWord(songName: String): String {

        if (songName.toLowerCase(Locale.US).contains("swift"))
        {
            return songName
        }
        for (ftName in featsList)
        {
            if (songName.toLowerCase().contains(ftName)) {
                Log.d(TAG, "index of ft: " + songName.indexOf("ft"))

                val indexOfFT= songName.indexOf(ftName)
                val removeRange = songName.removeRange(indexOfFT, songName.length)
                return removeRange
            }
        }

        return songName
    }

    private fun filterSongNameFromRedundantWords(songName: String): String {
        for (i in 0..filterationWords.size-1) {
            if (songName.contains(filterationWords[i])) {
                val replace = songName.replace(filterationWords[i], "")
                return replace
            }

        }
        return songName
    }
}
