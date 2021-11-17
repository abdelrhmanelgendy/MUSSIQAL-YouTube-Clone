package com.example.musiqal.viewModels.viewStates

import com.example.musiqal.datamodels.youtube.converter.toaudio.YoutubeMp3ConverterData


sealed class YoutubeVideoToMp3StateFlow {
    class Success(val item: YoutubeMp3ConverterData) : YoutubeVideoToMp3StateFlow()
    object Idel : YoutubeVideoToMp3StateFlow()
    object Loading : YoutubeVideoToMp3StateFlow()
    class Failed(val errorMessgae: String) : YoutubeVideoToMp3StateFlow()
}
