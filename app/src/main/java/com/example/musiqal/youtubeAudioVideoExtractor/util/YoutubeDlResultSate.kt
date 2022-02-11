package com.example.musiqal.youtubeAudioVideoExtractor.util

import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultData
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultDataItem

object YoutubeDlResultSate {
    fun checkStateForSingle(youTubeDlExtractorResultDataItem: YouTubeDlExtractorResultDataItem) {
        checkNormalState(youTubeDlExtractorResultDataItem)
    }

    fun checkStateForMultiple(youTubeDlExtractorResultData: YouTubeDlExtractorResultData): YoutubeDlResultSate {
        for (youtubeItems in youTubeDlExtractorResultData) {
            checkNormalState(youtubeItems)
        }
        return this
    }

    private fun checkNormalState(youTubeDlExtractorResultDataItem: YouTubeDlExtractorResultDataItem) {
        if (checkIfItsVideo(youTubeDlExtractorResultDataItem)) {

        } else {
            checkIfItsAudio(youTubeDlExtractorResultDataItem)
        }
    }

    private fun checkIfItsVideo(youTubeDlExtractorResultDataItem: YouTubeDlExtractorResultDataItem): Boolean {
        val formatId = youTubeDlExtractorResultDataItem.format_id?.toInt()
        if (formatId in YoutubeDlResultFormatIDs.VIDEO_LIST) {
            youTubeDlExtractorResultDataItem.isItVideo =
                YoutubeDlResultFormatIDs.IsVideoState.VIDEO.msg
            return true
        }
        return false
    }

    private fun checkIfItsAudio(youTubeDlExtractorResultDataItem: YouTubeDlExtractorResultDataItem): Boolean {
        val formatId = youTubeDlExtractorResultDataItem.format_id?.toInt()
        if (formatId in YoutubeDlResultFormatIDs.AUDIO_LIST) {
            youTubeDlExtractorResultDataItem.isItVideo =
                YoutubeDlResultFormatIDs.IsVideoState.AUDIO.msg
            return true
        }
        youTubeDlExtractorResultDataItem.isItVideo =
            YoutubeDlResultFormatIDs.IsVideoState.UNKNOWN.msg
        return false
    }

    public fun assignAudioQualityToAudioFile(items: List<YouTubeDlExtractorResultDataItem>):
            List<YouTubeDlExtractorResultDataItem> {
        items.map { i ->
          val formatType:Int=getFormateTypeByFormateId(i.format_id!!)
            i.videoQualityId = formatType.toString()
        }
        return items
    }

    private fun getFormateTypeByFormateId(format: String): Int {
        val formatId = format.toInt()
        if (formatId in YoutubeDlResultFormatIDs.AUDIO_FORMATS_MAP.keys)
        {
            return YoutubeDlResultFormatIDs.AUDIO_FORMATS_MAP.getValue(formatId)
        }
        return YoutubeDlResultFormatIDs.AUDIO_NO_FORMATS

    }
}