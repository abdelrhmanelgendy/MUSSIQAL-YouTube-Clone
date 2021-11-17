package com.example.musiqal.youtubeAudioVideoExtractor.mvi.state

import com.example.musiqal.youtubeAudioVideoExtractor.model.LocalExtractedFileData
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultData

sealed class SingleExtractedDataStateView {
    class Success(val localExtractedFileData: LocalExtractedFileData) : SingleExtractedDataStateView()
    object Idel : SingleExtractedDataStateView()
    object Loading : SingleExtractedDataStateView()
    class Failed(val errorMessgae: String) : SingleExtractedDataStateView()
}
