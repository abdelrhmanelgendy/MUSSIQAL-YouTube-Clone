package com.example.musiqal.search.mvi

import com.example.musiqal.database.local.HistoryOfSearchingDao
import com.example.musiqal.datamodels.youtubeApiSearchForVideo.YoutubeApiRequest
import com.example.musiqal.search.database.SearchHistoryData
import com.example.musiqal.search.database.remot.SearchApi
import com.example.musiqal.util.Resource
import javax.inject.Inject

class SearchMainRepository @Inject constructor(
    val searchingDao:
    HistoryOfSearchingDao,
    val searchApi: SearchApi
) : SearchDefaultRepository {

    override suspend fun insertHistory(searchHistoryData: SearchHistoryData) {
        searchingDao.deleteHistoryByItsTitle(searchHistoryData.searchTitle)
        searchingDao.insertHistory(searchHistoryData)
    }

    override suspend fun deleteHistory(searchHistoryData: SearchHistoryData) {
        searchingDao.deleteHistoryByItsTitle(searchHistoryData.searchTitle)
    }

    override suspend fun getAllHistoryHistory(): Resource<List<SearchHistoryData>> {
        return try {
            Resource.Success(searchingDao.getAllSearchData())
        } catch (e: Exception) {
            Resource.Failed(e.message.toString())
        }
    }

    override suspend fun deleteAllSearchData() {
        searchingDao.deleteAllHistory()
    }

    override suspend fun searchForVideo(
        part: String,
        type: String,
        searchQuery: String,
        maxResult: String,
        videoCategoryId: String,
        api_key: String
    ): Resource<YoutubeApiRequest> {

        return try {
            val searchForVideo = searchApi.searchForVideo(
                part,
                type,
                searchQuery,
                maxResult,
                videoCategoryId,
                api_key
            )
            if (searchForVideo.isSuccessful) {
                return Resource.Success(searchForVideo.body()!!)
            } else {
                return Resource.Failed(searchForVideo.message().toString())
            }


        } catch (e: java.lang.Exception) {
            Resource.Failed(e.message.toString())
        }
    }


}