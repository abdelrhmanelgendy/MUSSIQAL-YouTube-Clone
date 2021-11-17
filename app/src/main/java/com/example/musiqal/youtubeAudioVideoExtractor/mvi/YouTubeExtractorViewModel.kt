package com.example.musiqal.youtubeAudioVideoExtractor.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiqal.util.Resource
import com.example.musiqal.youtubeAudioVideoExtractor.model.LocalExtractedFileData
import com.example.musiqal.youtubeAudioVideoExtractor.mvi.state.AllExtractedDataStateView
import com.example.musiqal.youtubeAudioVideoExtractor.mvi.state.SingleExtractedDataStateView

import com.example.musiqal.youtubeAudioVideoExtractor.mvi.state.YoutubeURLSViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YouTubeExtractorViewModel @Inject constructor(
    val youtubeExtractorMainRepository:
    YoutubeExtractorMainRepository
) :
    ViewModel() {

    private var _audioStateFlow: MutableStateFlow<YoutubeURLSViewState> =
        MutableStateFlow(YoutubeURLSViewState.Idel)
    val audioStateFlow: StateFlow<YoutubeURLSViewState> = _audioStateFlow

    private var _allExtractedDataStateFlow: MutableStateFlow<AllExtractedDataStateView> =
        MutableStateFlow(AllExtractedDataStateView.Idel)
    val allExtractedDataStateFlow: StateFlow<AllExtractedDataStateView> = _allExtractedDataStateFlow

    private var _singleExtractedDataStateFLow: MutableStateFlow<SingleExtractedDataStateView> =
        MutableStateFlow(SingleExtractedDataStateView.Idel)
    val singleExtractedDataStateFLow: StateFlow<SingleExtractedDataStateView> = _singleExtractedDataStateFLow






    fun getAudio(videoId:String)
    {
        _audioStateFlow.value=YoutubeURLSViewState.Loading
        val videoUrl=getVideoUrl(videoId)
        viewModelScope.launch(Dispatchers.IO) {
            val audiosResult = youtubeExtractorMainRepository
                .getAudios(videoUrl)
            when(audiosResult)
            {
                is Resource.Success->_audioStateFlow.value=YoutubeURLSViewState.Success(audiosResult.data!!)
                is Resource.Failed->_audioStateFlow.value=YoutubeURLSViewState.Failed(audiosResult.message!!)

            }

        }

    }

    fun getSingleExtractedDataById(videoId:String)
    {
        _singleExtractedDataStateFLow.value=SingleExtractedDataStateView.Loading
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val extractionById = youtubeExtractorMainRepository.findExtractionById(videoId)
                _singleExtractedDataStateFLow.value=SingleExtractedDataStateView.Success(extractionById.data!!)
            }.onFailure {
                _singleExtractedDataStateFLow.value=SingleExtractedDataStateView.Failed(it.message!!)
            }
        }
    }

    fun getallExtractedData()
    {
        _allExtractedDataStateFlow.value=AllExtractedDataStateView.Loading
        viewModelScope.launch (Dispatchers.IO){
            kotlin.runCatching {
                val extractedData = youtubeExtractorMainRepository.getAllExtractions()
                _allExtractedDataStateFlow.value=AllExtractedDataStateView.Success(extractedData)
            }.onFailure {
                _allExtractedDataStateFlow.value=AllExtractedDataStateView.Failed(it.message!!)
            }
        }
    }

    fun deleteExtraction(videoId:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
           youtubeExtractorMainRepository.deleteExtraction(videoId)
        }
    }
    fun insertExtraction(localExtractedFileData: LocalExtractedFileData)
    {
        viewModelScope.launch(Dispatchers.IO) {
            youtubeExtractorMainRepository.insertExtraction(localExtractedFileData)
        }
    }

















    private fun getVideoUrl(videoId:String):String
    {
        val youtubeBaseUrl="https://youtu.be/"
        return youtubeBaseUrl+videoId
    }


}