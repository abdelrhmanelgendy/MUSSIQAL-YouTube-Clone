package com.example.musiqal.viewModels.viewStates

import com.example.musiqal.models.youtubeApiSearchForPlayList.Item
import com.example.musiqal.models.youtubeApiSearchForPlayList.Snippet
import com.example.musiqal.models.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest
import com.example.musiqal.models.youtubeApiSearchForVideo.YoutubeApiRequest

sealed class SavedPlayedTracksViewState {
    class Success(val itemLists: List<com.example.musiqal.models.youtubeItemInList.Item>) : SavedPlayedTracksViewState()
    object Idel : SavedPlayedTracksViewState()
    object Loading : SavedPlayedTracksViewState()
    class Failed(val errorMessgae: String) : SavedPlayedTracksViewState()
}
