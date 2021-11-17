package com.example.musiqal.search.mvi

import com.example.musiqal.datamodels.youtubeApiSearchForVideo.YoutubeApiRequest
import com.example.musiqal.search.database.SearchHistoryData
import com.example.musiqal.util.Resource

interface SearchDefaultRepository {

    suspend fun insertHistory(searchHistoryData: SearchHistoryData)
    suspend fun deleteHistory(searchHistoryData: SearchHistoryData)
    suspend fun getAllHistoryHistory():Resource<List<SearchHistoryData>>
    suspend fun deleteAllSearchData()
    suspend fun searchForVideo(
        part: String,
        type: String,
        searchQuery: String,
        maxResult: String,
        videoCategoryId: String,
        api_key: String
    ): Resource<YoutubeApiRequest>
}