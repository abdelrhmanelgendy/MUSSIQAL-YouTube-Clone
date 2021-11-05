package com.example.musiqal.viewModels.viewStates

import com.example.musiqal.models.youtubeApiSearchForPlayList.Item
import com.example.musiqal.models.youtubeApiSearchForPlayList.Snippet
import com.example.musiqal.models.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest
import com.example.musiqal.models.youtubeApiSearchForVideo.YoutubeApiRequest

sealed class DataResultViewState<T>(val data: T?, val message: String?) {
    class Success<T>(data: T?) : DataResultViewState<T>(data, null)
    class Idel<T> : DataResultViewState<T>(null, null)
    class Loading<T> : DataResultViewState<T>(null, null)
    class Failed<T>(val errorMessgae: String) : DataResultViewState<T>(null, errorMessgae)
}
