package com.example.musiqal.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest
import com.example.musiqal.repository.YoutubeMainRepository
import com.example.musiqal.util.Resource
import com.example.musiqal.viewModels.viewStates.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListsViewModel @Inject constructor(private val mainRepository: YoutubeMainRepository) :
    ViewModel() {

    var _latestPlayLitsStateFlow: MutableStateFlow<PlayListSearchViewState> =
        MutableStateFlow(PlayListSearchViewState.Idel)
    var _randomPlayLitsStateFlow: MutableStateFlow<PlayListSearchViewState> =
        MutableStateFlow(PlayListSearchViewState.Idel)

    var _popularPlayLitsStateFlow: MutableStateFlow<PlayListSearchViewState> =
        MutableStateFlow(PlayListSearchViewState.Idel)

    var _singer1PlayLitsStateFlow: MutableStateFlow<PlayListSearchViewState> =
        MutableStateFlow(PlayListSearchViewState.Idel)

    var _singer2PlayLitsStateFlow: MutableStateFlow<PlayListSearchViewState> =
        MutableStateFlow(PlayListSearchViewState.Idel)

    fun serchForRandomPlaylists(
        part: String,
        type: String,
        searchQuery: String,
        maxResult: String,
        videoCategoryId: String,
        api_key: String, mutableStateFlow: MutableStateFlow<PlayListSearchViewState>
    ) {
        Log.d("TAG", "searchForPlayList: ")
        viewModelScope.launch(Dispatchers.IO) {
            mutableStateFlow.value = PlayListSearchViewState.Loading()
            val searchResultResource = mainRepository.searchForPlayList(
                part,
                type,
                searchQuery,
                maxResult,
                videoCategoryId,
                api_key
            )
            when (searchResultResource) {
                is Resource.Success -> mutableStateFlow.value =
                    PlayListSearchViewState.Success(searchResultResource.data!!, true)
                is Resource.Failed -> mutableStateFlow.value =
                    PlayListSearchViewState.Failed(searchResultResource.message!!)
            }
        }
    }

    fun getRandomsFromDataBase(
        playListsId: Int,
        mutableStateFlow: MutableStateFlow<PlayListSearchViewState>
    ) {
        Log.d("TAG200", "getRandomsFromDataBase: ")
        viewModelScope.launch(Dispatchers.IO) {
            mutableStateFlow.value = PlayListSearchViewState.Loading()
            val searchResultResource = mainRepository.selectSavedYoutubaPlayList(playListsId)
            when (searchResultResource) {
                is Resource.Success -> mutableStateFlow.value =
                    PlayListSearchViewState.Success(searchResultResource.data!!,false)
                is Resource.Failed -> mutableStateFlow.value =
                    PlayListSearchViewState.Failed(searchResultResource.message!!)
            }
        }
    }
    fun deletePlayList(id:Int) {
        Log.d("TAG200", "deletePlayList: ")
        viewModelScope.launch {
            mainRepository.deleteYoutubePlayList(id)
        }
    }

    fun insertPlaylist(item: YoutubeApiSearchForPlayListRequest) {
        Log.d("TAG200", "insert playLists: ")
        viewModelScope.launch {
            mainRepository.insertYoutubePlayList(item)
        }
    }

}