package com.example.musiqal.viewModels.viewStates


sealed class YoutubeVideoDurationViewState {
    class Success(val duration: List<String>) : YoutubeVideoDurationViewState()
    object Idel : YoutubeVideoDurationViewState()
    object Loading : YoutubeVideoDurationViewState()
    class Failed(val errorMessgae: String) : YoutubeVideoDurationViewState()
}
