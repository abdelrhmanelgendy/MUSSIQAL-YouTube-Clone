package com.example.musiqal.search

import com.example.musiqal.search.database.SearchHistoryData

interface OnHistoryDataClickListener {
    fun onDataSearchClick(searchHistoryData: SearchHistoryData, position: Int)
    fun onTopArrowClick(searchHistoryData: SearchHistoryData, position: Int)
    fun onDataSearchLongClick(
        listOfSearchData: List<SearchHistoryData>,
        searchHistoryData: SearchHistoryData,
        position: Int
    )
}
