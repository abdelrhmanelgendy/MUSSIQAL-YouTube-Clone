package com.example.musiqal.userPLaylists.mvi

import com.example.musiqal.userPLaylists.model.UserPlayList

interface UserPlayListsDefaultRepository {


    suspend fun insertNewPLayList(userPlayList: UserPlayList)
    suspend fun deletePlayListByName(playListName: String)
    suspend fun getPlayListByItsName(playListName: String): UserPlayList
    suspend fun getAllPlayLists(): List<UserPlayList>
    suspend fun updatePlayListCover(playListName: String, newPlayListCover: String)
    suspend fun updatePlayListLastUpdatedDate(playListName: String, lastUpdated: String)
    suspend fun getALlPlayListsNames():List<String>
}