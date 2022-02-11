package com.example.musiqal.lyrics.lyricsdatabase.local.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiqal.lyrics.model.LyricsData
import com.example.musiqal.lyrics.model.LyricsLocalDataModel
import com.example.musiqal.lyrics.mvi.LyricsMainRepository
import com.example.musiqal.lyrics.mvi.LyricsViewState
import com.example.musiqal.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LyricsDatabaseViewModel @Inject constructor(
    private val
    lyricsDataBaseMainRepository: LyricsDataBaseMainRepository
) :
    ViewModel() {

    private var _listOfLyricsStateFlow: MutableStateFlow<LyricsDatabaseViewState> =
        MutableStateFlow(LyricsDatabaseViewState.Idel)
     var listOfLyricsStateFlow: StateFlow<LyricsDatabaseViewState> = _listOfLyricsStateFlow


    fun getAllTracksLyrics() {
        viewModelScope.launch {
            _listOfLyricsStateFlow.emit(LyricsDatabaseViewState.Loading)

            val allLyricsData = lyricsDataBaseMainRepository.getAllLyricsData()

            when (allLyricsData) {

                is Resource.Success -> {
                    _listOfLyricsStateFlow.emit(LyricsDatabaseViewState.Success(allLyricsData.data!!))
                }
                is Resource.Failed -> {
                    _listOfLyricsStateFlow.emit(LyricsDatabaseViewState.Failed(allLyricsData.message!!))

                }

            }
        }
    }

    fun deleteLyricsById(id: Int) {
        viewModelScope.launch {
            lyricsDataBaseMainRepository.deleteLyricsData(id)
        }
    }

    fun insertTrackLyrics(lyricsLocalDataModel: LyricsLocalDataModel) {
        viewModelScope.launch {
            Log.d("TAG", "adding02: ")

            lyricsDataBaseMainRepository.insertLyricsData(lyricsLocalDataModel)
        }
    }

    sealed class LyricsDatabaseViewState {
        class Success(val lyricsLocalDataModels: List<LyricsLocalDataModel>) :
            LyricsDatabaseViewState()

        object Idel : LyricsDatabaseViewState()
        object Loading : LyricsDatabaseViewState()
        class Failed(val errorMessgae: String) : LyricsDatabaseViewState()
    }

}