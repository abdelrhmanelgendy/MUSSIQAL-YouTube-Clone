package com.example.musiqal.viewModels.viewStates

import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest

sealed class PlayListSearchViewState {
    class Success(
        val youtubeApiRequest: YoutubeApiSearchForPlayListRequest,
        val isFromInternet: Boolean
    ) : PlayListSearchViewState()

    object Idel : PlayListSearchViewState()
    class Loading : PlayListSearchViewState()
    class Failed(val errorMessgae: String) : PlayListSearchViewState()
}
