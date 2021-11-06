package com.example.musiqal.lyrics.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiqal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricsViewModel @Inject constructor(val lyricsMainRepository: LyricsMainRepository) :
    ViewModel() {
    private var _songLyricsStateFlow: MutableStateFlow<LyricsViewState> =
        MutableStateFlow(LyricsViewState.Idel)
    val songLyricsStateFlow: StateFlow<LyricsViewState> = _songLyricsStateFlow


    fun searchForSongLyrics(artistName: String, songName: String) {
        Log.d("TAG", "searchForSongLyrics: ")
        _songLyricsStateFlow.value = LyricsViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val songLyricsResource = lyricsMainRepository.getSongLyrice(artistName, songName)
            when (songLyricsResource) {
                is Resource.Success -> _songLyricsStateFlow.value =
                    LyricsViewState.Success(songLyricsResource.data!!)
                is Resource.Failed -> _songLyricsStateFlow.value =
                    LyricsViewState.Failed(songLyricsResource.message!!)
            }
        }
    }
}