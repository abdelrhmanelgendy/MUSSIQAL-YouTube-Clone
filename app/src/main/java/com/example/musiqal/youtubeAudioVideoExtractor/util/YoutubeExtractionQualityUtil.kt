package com.example.musiqal.youtubeAudioVideoExtractor.util

import android.util.Log
import com.example.musiqal.util.UrlAvailability
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultData
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultDataItem

object YoutubeExtractionQualityUtil {
    fun getMinimumAudioQuality(
        youtubeUrl: YouTubeDlExtractorResultData,
        urlIndex: Int = 0
    ): String {
        Log.d("TAG", "getMinimumAudioQuality: "+urlIndex)
        val filter =
            youtubeUrl.filter { i -> i.isItVideo.equals(YoutubeDlResultFormatIDs.IsVideoState.AUDIO.msg) }
        val sorted = filter.sortedBy { i -> i.filesize!! }
        if (sorted.size == 0) {
            return "-1"
        }
        Log.d("TAG", "getMinimumAudioQuality: ${sorted.toString()}")
        val selectedLinkBasedOnMinimumSize = sorted.get(urlIndex)
        val audioAvailability =
            UrlAvailability.checkAudioAvailability(selectedLinkBasedOnMinimumSize.url!!)
        if (audioAvailability) {
            return sorted.get(0).url!!
        } else {
            if (urlIndex == sorted.size - 1) {
                return "-1"
            }
            else{

               return sorted.get(1).url!!
            }
        }

    }

    fun getMinimumVideoQuality(youtubeUrl: YouTubeDlExtractorResultData): YouTubeDlExtractorResultDataItem {
        val filter =
            youtubeUrl.filter { i -> i.isItVideo.equals(YoutubeDlResultFormatIDs.IsVideoState.VIDEO.msg) }
        val sorted = filter.sortedBy { i -> i.filesize!! }

        return sorted.get(0)
    }

    fun getMaxAudioQuality(youtubeUrl: YouTubeDlExtractorResultData): String {
        val filter =
            youtubeUrl.filter { i -> i.isItVideo.equals(YoutubeDlResultFormatIDs.IsVideoState.AUDIO.msg) }
        val sorted = filter.sortedBy { i -> i.filesize!! }
        val selectedLinkBasedOnMinimumSize = sorted.get(0)
        val audioAvailability =
            UrlAvailability.checkAudioAvailability(selectedLinkBasedOnMinimumSize.url!!)
        if (audioAvailability) {
            return sorted.get(0).url!!
        } else {
            return "-1"
        }
    }


    fun getMaxVideoQuality(youtubeUrl: YouTubeDlExtractorResultData): YouTubeDlExtractorResultDataItem {
        val filter =
            youtubeUrl.filter { i -> i.isItVideo.equals(YoutubeDlResultFormatIDs.IsVideoState.VIDEO.msg) }
        val sorted = filter.sortedBy { i -> i.filesize!! }

        return sorted.get(sorted.size - 1)
    }

    fun getMediumAudioQuality(youtubeUrl: YouTubeDlExtractorResultData): String {
        val filter =
            youtubeUrl.filter { i -> i.isItVideo.equals(YoutubeDlResultFormatIDs.IsVideoState.AUDIO.msg) }
        val sorted = filter.sortedBy { i -> i.filesize!! }
        val selectedLinkBasedOnMinimumSize = sorted.get(0)
        val audioAvailability =
            UrlAvailability.checkAudioAvailability(selectedLinkBasedOnMinimumSize.url!!)
        if (audioAvailability) {
            return sorted.get(0).url!!
        } else {
            return "-1"
        }
    }

    fun getMedduimVideoQuality(youtubeUrl: YouTubeDlExtractorResultData): YouTubeDlExtractorResultDataItem {
        val filter =
            youtubeUrl.filter { i -> i.isItVideo.equals(YoutubeDlResultFormatIDs.IsVideoState.VIDEO.msg) }
        val sorted = filter.sortedBy { i -> i.filesize!! }

        return sorted.get(((sorted.size - 1) / 2).toInt())

    }
}