package com.example.musiqal.repository

import com.example.musiqal.lyrics.model.LyricsData
import com.example.musiqal.models.youtube.converter.toaudio.YoutubeMp3ConverterData
import com.example.musiqal.models.youtubeApiChannel.YoutubeCategoryRequest
import com.example.musiqal.models.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest
import com.example.musiqal.models.youtubeItemInList.Item
import com.example.musiqal.models.youtubeItemInList.YoutubeVideosInPlaylistRequest
import com.example.musiqal.util.Resource

interface YoutubeDefaultRepository {


    suspend fun searchForPlayList(
        part: String,
        type: String,
        searchQuery: String,
        maxResult: String,
        categoryId: String,
        api_key: String
    ): Resource<YoutubeApiSearchForPlayListRequest>

    suspend fun getVideosInPlayList(
        part: String,
        playListId: String,
        maxResult: String,
        api_key: String
    ): Resource<YoutubeVideosInPlaylistRequest>


    suspend fun getYoutubeCategoryId(
        videoPart: String,
        regionCode: String,
        apiKey: String
    ): Resource<YoutubeCategoryRequest>

    suspend fun getVideoMp3ConvertedUrl(
        rapidHost: String,
        rapidApiKey: String,
        videoId: String
    ): Resource<YoutubeMp3ConverterData>


    suspend fun getSongLyrice(artistName: String, songName: String): Resource<LyricsData>

    suspend fun deleteYoutubePlayList(id: Int)
    suspend fun insertYoutubePlayList(youtubeApiSearchForPlayListRequest: YoutubeApiSearchForPlayListRequest)
    suspend fun selectSavedYoutubaPlayList(playListId: Int): Resource<YoutubeApiSearchForPlayListRequest>


    suspend fun insertPlayedTrack(item:Item)
    suspend fun deletePlayedTrack(itemId:Int)
    suspend fun getAllPlayedTracks():Resource<List<Item>>
}