package com.example.musiqal.downloadManager.util

import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultDataItem

interface OnSelectedTrackClickedListener {
    fun onTrackSeelcted(currentSelectedItem: YouTubeDlExtractorResultDataItem)
}