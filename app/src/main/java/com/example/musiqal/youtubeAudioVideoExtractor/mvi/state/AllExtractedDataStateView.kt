package com.example.musiqal.youtubeAudioVideoExtractor.mvi.state

import com.example.musiqal.youtubeAudioVideoExtractor.model.LocalExtractedFileData
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultData

sealed class AllExtractedDataStateView {
    class Success(val localExtractedFileData: List<LocalExtractedFileData>) : AllExtractedDataStateView()
    object Idel : AllExtractedDataStateView()
    object Loading : AllExtractedDataStateView()
    class Failed(val errorMessgae: String) : AllExtractedDataStateView()
}
