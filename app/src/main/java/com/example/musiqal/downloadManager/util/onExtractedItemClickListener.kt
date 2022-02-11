package com.example.musiqal.downloadManager.util

import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultDataItem

public interface onExtractedItemClickListener {
    fun onClick(
        youTubeDlExtractorResultDataItem: YouTubeDlExtractorResultDataItem,
        adapterPosition: Int
    )
}