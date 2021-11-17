package com.example.musiqal.viewModels.viewStates

import com.example.musiqal.datamodels.youtubeApiChannel.YoutubeCategoryRequest

sealed class YoutubeCategoryViewState {
    class Success(val youtubeCategoryRequest: YoutubeCategoryRequest) : YoutubeCategoryViewState()
    object Idel : YoutubeCategoryViewState()
    object Loading : YoutubeCategoryViewState()
    class Failed(val errorMessgae: String) : YoutubeCategoryViewState()
}
