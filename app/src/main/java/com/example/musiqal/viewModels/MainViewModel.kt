package com.example.musiqal.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiqal.lyrics.mvi.LyricsViewState
import com.example.musiqal.models.youtubeItemInList.Item
import com.example.musiqal.repository.YoutubeMainRepository
import com.example.musiqal.util.CustomeMusicPlayback
import com.example.musiqal.util.Resource
import com.example.musiqal.viewModels.viewStates.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: YoutubeMainRepository) :
    ViewModel() {


    private var _youtubeCategoryStateFlow: MutableStateFlow<YoutubeCategoryViewState> =
        MutableStateFlow(YoutubeCategoryViewState.Idel)
    val youtubeCategoryStateFlow: StateFlow<YoutubeCategoryViewState> = _youtubeCategoryStateFlow


    private var _itemInPlayLitsStateFlow: MutableStateFlow<VideosInPlayListViewState> =
        MutableStateFlow(VideosInPlayListViewState.Idel)
    val itemInPlayLitsStateFlow: StateFlow<VideosInPlayListViewState> = _itemInPlayLitsStateFlow

    private var _savedPlayListStateFlow: MutableStateFlow<SavedPlayListViewState> =
        MutableStateFlow(SavedPlayListViewState.Idel)
    val savedPlayListStateFlow: StateFlow<SavedPlayListViewState> = _savedPlayListStateFlow

    private var _youtubeVideoToMp3StateFlow: MutableStateFlow<YoutubeVideoToMp3StateFlow> =
        MutableStateFlow(YoutubeVideoToMp3StateFlow.Idel)
    val youtubeVideoToMp3StateFlow: StateFlow<YoutubeVideoToMp3StateFlow> =
        _youtubeVideoToMp3StateFlow


    private var _savedPlayedTraksStateFlow: MutableStateFlow<SavedPlayListViewState> =
        MutableStateFlow(SavedPlayListViewState.Idel)

    val savedPlayedTraksStateFlow: StateFlow<SavedPlayListViewState> =
        _savedPlayedTraksStateFlow




    fun searchForYoutubeCategory(
        videoPart: String,
        regionCode: String,
        apiKey: String
    ) {
        _youtubeCategoryStateFlow.value = YoutubeCategoryViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val youtubeCategoriesResource =
                mainRepository.getYoutubeCategoryId(videoPart, regionCode, apiKey)
            when (youtubeCategoriesResource) {
                is Resource.Success -> _youtubeCategoryStateFlow.value =
                    YoutubeCategoryViewState.Success(youtubeCategoriesResource.data!!)
                is Resource.Failed -> _youtubeCategoryStateFlow.value =
                    YoutubeCategoryViewState.Failed(youtubeCategoriesResource.message!!)

            }
        }
    }




    fun getVideosInsidePlaylist(
        part: String,
        playListId: String,
        maxResult: String,
        api_key: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _itemInPlayLitsStateFlow.value = VideosInPlayListViewState.Loading
            val searchResultResource = mainRepository.getVideosInPlayList(
                part,
                playListId,
                maxResult,
                api_key
            )
            when (searchResultResource) {
                is Resource.Success -> _itemInPlayLitsStateFlow.value =
                    VideosInPlayListViewState.Success(searchResultResource.data!!)
                is Resource.Failed -> _itemInPlayLitsStateFlow.value =
                    VideosInPlayListViewState.Failed(searchResultResource.message!!)
            }
        }
    }

    fun getMp3VideoConvertedUrl(
        rapidHost: String,
        rapidKey: String,
        videoId: String
    ) {
        Log.d("convertVideoToUrl", "getMp3VideoConvertedUrl: Hit Api")
        viewModelScope.launch(Dispatchers.IO) {
            _youtubeVideoToMp3StateFlow.value = YoutubeVideoToMp3StateFlow.Loading
            val videoCoverted = mainRepository.getVideoMp3ConvertedUrl(
                rapidHost, rapidKey, videoId
            )
            when (videoCoverted) {
                is Resource.Success -> _youtubeVideoToMp3StateFlow.value =
                    YoutubeVideoToMp3StateFlow.Success(videoCoverted.data!!)
                is Resource.Failed -> _youtubeVideoToMp3StateFlow.value =
                    YoutubeVideoToMp3StateFlow.Failed(videoCoverted.message!!)
            }
        }
    }

    fun provideSingleMediaPlayerInstance(): CustomeMusicPlayback {
        val print = mainRepository.provideCustomeMusicPlayback()
        return print
    }


    fun insertSavedTrackItem(item: Item) {
        Log.d("TAG", "insertSavedTrackItem: ")
        viewModelScope.launch {
            mainRepository.insertPlayedTrack(item)
        }
    }

    fun deleteSavedTrackItem(id: Int) {
        viewModelScope.launch {
            mainRepository.deletePlayedTrack(id)
        }

    }

    fun getAllPLayedTrackHistory() {
        _savedPlayedTraksStateFlow.value = SavedPlayListViewState.Idel
        viewModelScope.launch(Dispatchers.IO) {
            _savedPlayedTraksStateFlow.value = SavedPlayListViewState.Loading()
            val savedTracks = mainRepository.getAllPlayedTracks()
            when (savedTracks) {
                is Resource.Success -> _savedPlayedTraksStateFlow.value =
                    SavedPlayListViewState.Success(savedTracks.data!!)
                is Resource.Failed -> _savedPlayedTraksStateFlow.value =
                    SavedPlayListViewState.Failed(savedTracks.message!!)
            }
        }
    }
}