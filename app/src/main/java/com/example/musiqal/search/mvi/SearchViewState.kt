package com.example.musiqal.search.mvi

import com.example.musiqal.search.database.SearchHistoryData

sealed class SearchViewState {
    class Success(val listOfSearchData: List<SearchHistoryData>) : SearchViewState()
    object Idel : SearchViewState()
    object Loading : SearchViewState()
    class Failed(val errorMessgae: String) : SearchViewState()
}
