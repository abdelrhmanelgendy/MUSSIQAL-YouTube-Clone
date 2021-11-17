package com.example.musiqal.viewModels.viewStates

import com.example.musiqal.datamodels.youtubeItemInList.Item


sealed class SavedPlayListViewState {
    class Success(val item: List<Item>) : SavedPlayListViewState()
    object Idel : SavedPlayListViewState()
    class Loading : SavedPlayListViewState()
    class Failed(val errorMessgae: String) : SavedPlayListViewState()
}
