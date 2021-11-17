package com.example.musiqal.viewModels.viewStates

import com.example.musiqal.datamodels.youtubeItemInList.YoutubeVideosInPlaylistRequest

sealed class VideosInPlayListViewState {
    class Success(val youtubeVideosInPlaylistRequest: YoutubeVideosInPlaylistRequest) : VideosInPlayListViewState()
    object Idel : VideosInPlayListViewState()
    object Loading : VideosInPlayListViewState()
    class Failed(val errorMessgae: String) : VideosInPlayListViewState()
}
