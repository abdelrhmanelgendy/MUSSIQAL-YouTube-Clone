package com.example.musiqal.userPLaylists.mvi

import com.example.musiqal.userPLaylists.database.UserPlaylistDao
import com.example.musiqal.userPLaylists.model.UserPlayList
import javax.inject.Inject

class UserPlayListsMainRepository
@Inject constructor(val userPlaylistDao: UserPlaylistDao) :
    UserPlayListsDefaultRepository {
    override suspend fun insertNewPLayList(userPlayList: UserPlayList) {
        userPlaylistDao.insertNewPLayList(userPlayList)
    }

    override suspend fun deletePlayListByName(playListName: String) {
        userPlaylistDao.deletePlayListByName(playListName)
    }

    override suspend fun getPlayListByItsName(playListName: String): UserPlayList {
        return userPlaylistDao.getPlayListByItsName(playListName)
    }

    override suspend fun getAllPlayLists(): List<UserPlayList> {
        return userPlaylistDao.getAllPlayLists()
    }

    override suspend fun updatePlayListCover(playListName: String, newPlayListCover: String) {
//        userPlaylistDao.updatePlayListCover(playListName, newPlayListCover)
    }

    override suspend fun updatePlayListLastUpdatedDate(playListName: String, lastUpdated: String) {
        userPlaylistDao.updatePlayListLastUpdatedDate(playListName, lastUpdated)
    }

    override suspend fun getALlPlayListsNames(): List<String> {
        return userPlaylistDao.getAllPlaylistsNames()
    }

}