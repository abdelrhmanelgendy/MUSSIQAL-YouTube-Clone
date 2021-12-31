package com.example.musiqal.extractVideoDirectLink

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.musiqal.extractVideoDirectLink.util.FileQuality
import com.example.musiqal.extractVideoDirectLink.util.VideoExtractionEventListener
import com.example.musiqal.ui.MainActivity
import com.example.musiqal.util.Constants
import com.example.musiqal.util.UrlAvailability
import com.example.musiqal.viewModels.viewStates.YoutubeVideoToMp3StateFlow
import com.example.musiqal.youtubeAudioVideoExtractor.model.LocalExtractedFileData
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultData
import com.example.musiqal.youtubeAudioVideoExtractor.mvi.YouTubeExtractorViewModel
import com.example.musiqal.youtubeAudioVideoExtractor.mvi.state.AllExtractedDataStateView
import com.example.musiqal.youtubeAudioVideoExtractor.mvi.state.YoutubeURLSViewState
import com.example.musiqal.youtubeAudioVideoExtractor.util.YoutubeDlResultSate
import com.example.musiqal.youtubeAudioVideoExtractor.util.YoutubeExtractionQualityUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest

class VideoLinkToDirectUrlExtraction(
    private val context: Context,
    val videoExtractionEventListener: VideoExtractionEventListener,
    private val usertType: String
) {
    companion object {
        val PREMUIM_USER_TYPE = "premuim_user"
        val NORMAL_USER_TYPE = "normal_user"

    }
    var isVideoSuucesFromPremuimUSer=false

    private val TAG = "VideoExtraction"


    fun getVideoLink(videoId: String) {
        getOldVideoFromDataBase(videoId)
    }

    private fun getOldVideoFromDataBase(videoId: String) {
        val youTubeExtractorViewModel = (context as MainActivity).youTubeExtractorViewModel
        youTubeExtractorViewModel.getallExtractedData()

        CoroutineScope(Dispatchers.Default).launch {
            youTubeExtractorViewModel.allExtractedDataStateFlow.collectIndexed { index, value ->
                when (value) {
                    is AllExtractedDataStateView.Loading -> {
                        Log.d(TAG, "getVideoLinkByVideoId: loading")
                    }
                    is AllExtractedDataStateView.Success -> {
                        if (index == 1) {
                            Log.d(TAG, "getOldVideoFromDataBase: " + videoId)
                            checkIfDatabaseHaveTheVideoUrl(videoId, value.localExtractedFileData)
                        }
                    }
                    is AllExtractedDataStateView.Failed -> {
                        Log.d(TAG, "getVideoLinkByVideoId: " + value.errorMessgae)
                    }

                }

            }
        }
    }

    private fun checkIfDatabaseHaveTheVideoUrl(
        videoId: String,
        localExtractedFileData: List<LocalExtractedFileData>
    ) {
        Log.d(TAG, "checkIfDatabaseHaveTheVideoUrl: " + localExtractedFileData.size)
        if (localExtractedFileData.size == 0) {
            extractVideoLinkByVideoId(videoId)
        }
        for (i in 0..localExtractedFileData.size - 1) {
            if (videoId.equals(localExtractedFileData[i].videId)) {
                Log.d(
                    TAG,
                    "checkIfDatabaseHaveTheVideoUrl: $videoId ${localExtractedFileData[i].videId}"
                )
                val mp3Url = localExtractedFileData[i].mp3Url
                val urlAvailability = UrlAvailability.checkUrl(mp3Url)
                if (urlAvailability.isAvailable) {
                    videoExtractionEventListener.onSuccess(mp3Url)
                    Log.d(TAG, "checkIfDatabaseHaveTheVideoUrl: availabile")
                } else {
                    extractVideoLinkByVideoId(videoId)
                    Log.d(TAG, "checkIfDatabaseHaveTheVideoUrl: NOTavailabile")
                }
                Log.d(TAG, "checkIfDatabaseHaveTheVideoUrl: " + urlAvailability)
                break
            }
            if (i == localExtractedFileData.size - 1) {
                Log.d(TAG, "checkIfDatabaseHaveTheVideoUrl: un available finish")
                extractVideoLinkByVideoId(videoId)
            }


        }


    }


    private fun extractVideoLinkByVideoId(videoId: String) {
        val premuimUser = usertType.equals(PREMUIM_USER_TYPE)
        if (premuimUser) {
            findForPremuimUser(videoId)
        } else {
            findForReqularSlowUser(videoId)
        }


    }



    private fun filterResults(youtubeUrl: YouTubeDlExtractorResultData, videoId: String) {
        val savedFileQuality = getSavedFileQualityFromSharedPref()
        YoutubeDlResultSate.checkStateForMultiple(youtubeUrl)
        val audioQuality = getAudioFileBasedOnFileQualityTag(savedFileQuality, youtubeUrl)
        videoExtractionEventListener.onSuccess(audioQuality)
        insertExtractedVideo(videoId, audioQuality)
    }

    private fun getAudioFileBasedOnFileQualityTag(
        audioQuality: String,
        youtubeUrl: YouTubeDlExtractorResultData
    ): String {

        when (audioQuality) {
           FileQuality.QualityLow.msg-> return   YoutubeExtractionQualityUtil.getMinimumAudioQuality(
                youtubeUrl)

            FileQuality.QualityHigh.msg ->
                return YoutubeExtractionQualityUtil.getMaxAudioQuality(youtubeUrl)
            FileQuality.QualityMeduim.msg ->
                return YoutubeExtractionQualityUtil.getMediumAudioQuality(youtubeUrl)
            else -> return YoutubeExtractionQualityUtil.getMinimumAudioQuality(youtubeUrl)

        }

    }

    private fun getSavedFileQualityFromSharedPref(): String {
        return FileQuality.QualityLow.msg
    }

    var nexExtractionStarted = false

    private fun findForReqularSlowUser(videoId: String) {
        Log.d(TAG, "findForReqularSlowUser: regular user")
        val youTubeExtractorViewModel =
            ViewModelProvider(context as AppCompatActivity).get(YouTubeExtractorViewModel::class.java)
        youTubeExtractorViewModel.getAudio(videoId)
        CoroutineScope(Dispatchers.Default).launch {
            youTubeExtractorViewModel.audioStateFlow.collectIndexed { index, value ->
                when (value) {
                    is YoutubeURLSViewState.Loading -> {
                        Log.d(TAG, "getVideoLinkByVideoId: loading")
                    }
                    is YoutubeURLSViewState.Success -> {
                        Log.d(TAG, "findForReqularSlowUser: gettingSucess1212"+value.youtubeUrl.toString())
                        if (index==1)
                        {
                            filterResults(value.youtubeUrl,videoId)
                            Log.d(TAG, "findForReqularSlowUser: success withindex"+value.youtubeUrl)
                        }

                    }
                    is YoutubeURLSViewState.Failed -> {
                        Log.d(TAG, "getVideoLinkByVideoId1212: " + value.errorMessgae)
                    }

                }

            }
        }

    }
    private fun findForPremuimUser(videoId: String) {

        val mainViewModel = (context as MainActivity).mainViewModel
        if (!nexExtractionStarted) {
            mainViewModel.getMp3VideoConvertedUrl(Constants.YOUTUBE_MP3_RapidHost,Constants.getRandomMp3Api(),videoId)
            CoroutineScope(Dispatchers.Default).launch {
                mainViewModel.youtubeVideoToMp3StateFlow.collectLatest { event ->

                    when (event) {
                        is YoutubeVideoToMp3StateFlow.Loading -> {
                            Log.d(TAG, "findForPremuimUser: loading")
                        }
                        is YoutubeVideoToMp3StateFlow.Success -> {
                            if (!isVideoSuucesFromPremuimUSer) {
                                Log.d(TAG, "findForPremuimUser: last index Success ${event.item}")
                                if (event.item.link.isEmpty()) {


                                    isVideoSuucesFromPremuimUSer=true
                                    findForReqularSlowUser(videoId)
                                } else
                                {
                                    insertExtractedVideo(videoId,event.item.link)
                                    videoExtractionEventListener.onSuccess(event.item.link)
                                    isVideoSuucesFromPremuimUSer=true
                                }

                            }
//                            Log.d(TAG, "findForPremuimUser:Success $index ${event.item.toString()}")
//                            if (index==1)
//                            {
//                                Log.d(TAG, "findForPremuimUser: last index Success ${event.item}")
//                                insertExtractedVideo(videoId,event.item.link)
//                                videoExtractionEventListener.onSuccess(event.item.link)
//
//                            }

                        }
                        is YoutubeVideoToMp3StateFlow.Failed -> {
                            Log.d(TAG, "findForPremuimUser: failed ${event.errorMessgae}")
                        }
                    }
                }
            }
        }


    }

    private fun changeMethodOfGettingUrl(videoId: String) {


    }

    private fun insertExtractedVideo(videoId: String, link: String) {
        val extractorViewModel = (context as MainActivity).youTubeExtractorViewModel
        extractorViewModel.deleteExtraction(videoId)
        extractorViewModel.insertExtraction(LocalExtractedFileData(videoId, link))

    }
}