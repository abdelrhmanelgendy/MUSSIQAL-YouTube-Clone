package com.example.musiqal.search.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiqal.search.database.SearchHistoryData
import com.example.musiqal.search.util.BufferedReaderExractor
import com.example.musiqal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject constructor(
    private val searchMainRepository:
    SearchMainRepository
) : ViewModel() {
    private  val TAG = "SearchViewModel11"

    private val _searchHistoryStateFlow: MutableStateFlow<SearchViewState> =
        MutableStateFlow(SearchViewState.Idel)
    val searchHistoryStateFlow: StateFlow<SearchViewState> = _searchHistoryStateFlow

    private var _youtubeSearchStateFlow: MutableStateFlow<YoutubeSearchViewState> =
        MutableStateFlow(YoutubeSearchViewState.Idel)
    val youtubeSearchStateFlow: StateFlow<YoutubeSearchViewState> = _youtubeSearchStateFlow


    fun deleteAllDate() {
        viewModelScope.launch(Dispatchers.IO) {
            searchMainRepository.deleteAllSearchData()
        }
    }

    fun deleteSearchHistory(searchHistoryData: SearchHistoryData) {
        viewModelScope.launch(Dispatchers.IO) {
            searchMainRepository.deleteHistory(searchHistoryData)
        }
    }

    fun insertHistoryData(searchHistoryData: SearchHistoryData) {
        viewModelScope.launch(Dispatchers.IO) {
            searchMainRepository.insertHistory(searchHistoryData)
        }
    }


    fun getAllSearchData() {
        Log.d(TAG, "getAllSearchData: ")
        _searchHistoryStateFlow.value = SearchViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val allHistoryHistory = searchMainRepository.getAllHistoryHistory()
            when (allHistoryHistory) {
                is Resource.Success -> _searchHistoryStateFlow.value =
                    SearchViewState.Success(allHistoryHistory.data!!)

                is Resource.Failed -> _searchHistoryStateFlow.value =
                    SearchViewState.Failed(allHistoryHistory.message!!)
            }

        }
    }

    fun getAutoCompleteQueries(query: String) {
        Log.d(TAG, "getAutoCompleteQueries: "+query)
        _searchHistoryStateFlow.value = SearchViewState.Loading
        try {
            val url =
                "http://suggestqueries.google.com/complete/search?client=firefox&ds=yt&q=$query"
            viewModelScope.launch(Dispatchers.Default) {
                val urlConnection = URL(url)
                val openStream = urlConnection.openStream()
                val inputStreamReader = InputStreamReader(openStream, Charset.forName("UTF-8"))
                val bufferedReader = BufferedReader(inputStreamReader)
                val readLine = BufferedReaderExractor.extractText(bufferedReader)
                val jsonObject = JSONArray(readLine)
                val stringArr = jsonObject.getJSONArray(1)

                val results = arrayListOf<String>()
                for (i in 0..stringArr.length() - 1) {

                    results.add(stringArr.get(i).toString())
                }
                val dataHistoryLists: List<SearchHistoryData> =
                    createListOfSearchDataHistory(results)
                _searchHistoryStateFlow.value = SearchViewState.Success(dataHistoryLists)
            }
        } catch (e: Exception) {
            _searchHistoryStateFlow.value = SearchViewState.Failed(e.toString())
        }


    }

    private fun createListOfSearchDataHistory(results: ArrayList<String>): List<SearchHistoryData> {
        return results.map { s -> (SearchHistoryData(s, "-1")) }

    }

    fun serchForVideos(
        part: String,
        type: String,
        searchQuery: String,
        maxResult: String,
        videoCategoryId: String,
        api_key: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _youtubeSearchStateFlow.value = YoutubeSearchViewState.Loading()
            val searchResultResource = searchMainRepository.searchForVideo(
                part,
                type,
                searchQuery,
                maxResult,
                videoCategoryId,
                api_key
            )
            when (searchResultResource) {
                is Resource.Success -> _youtubeSearchStateFlow.value =
                    YoutubeSearchViewState.Success(searchResultResource.data!!)
                is Resource.Failed -> _youtubeSearchStateFlow.value =
                    YoutubeSearchViewState.Failed(searchResultResource.message!!)
            }
        }
    }


}