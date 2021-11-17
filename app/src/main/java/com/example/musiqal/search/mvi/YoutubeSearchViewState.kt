package com.example.musiqal.search.mvi

import com.example.musiqal.datamodels.youtubeApiSearchForVideo.YoutubeApiRequest

sealed class YoutubeSearchViewState {
    class Success(val youtubeApiRequest: YoutubeApiRequest) : YoutubeSearchViewState()
    object Idel : YoutubeSearchViewState()
    class Loading : YoutubeSearchViewState()
    class Failed(val errorMessgae: String) : YoutubeSearchViewState()
}
