package com.example.musiqal.viewModels.viewStates

sealed class SavedPlayedTracksViewState {
    class Success(val itemLists: List<com.example.musiqal.datamodels.youtubeItemInList.Item>) : SavedPlayedTracksViewState()
    object Idel : SavedPlayedTracksViewState()
    object Loading : SavedPlayedTracksViewState()
    class Failed(val errorMessgae: String) : SavedPlayedTracksViewState()
}
