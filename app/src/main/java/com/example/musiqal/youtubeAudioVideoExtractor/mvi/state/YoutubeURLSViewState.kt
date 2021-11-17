package com.example.musiqal.youtubeAudioVideoExtractor.mvi.state

import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultData

sealed class YoutubeURLSViewState {
    class Success(val youtubeUrl: YouTubeDlExtractorResultData) : YoutubeURLSViewState()
    object Idel : YoutubeURLSViewState()
    object Loading : YoutubeURLSViewState()
    class Failed(val errorMessgae: String) : YoutubeURLSViewState()
}
