package com.example.musiqal.userPLaylists.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.userPLaylists.model.UserPlayList

@Dao
interface UserPlaylistDao {

    @Insert
    suspend fun insertNewPLayList(userPlayList: UserPlayList)

    @Query("delete from userPLayLists where playListName =:playListName")
    suspend fun deletePlayListByName(playListName: String)

    @Query("select * from userPLayLists where playListName=:playListName")
    suspend fun getPlayListByItsName(playListName: String): UserPlayList

    @Query("select * from userPLayLists")
    suspend fun getAllPlayLists(): List<UserPlayList>

    @Query("update userPLayLists set playListCoverUrl=:newPlayListCover where playListName=:playListName")
    suspend fun updatePlayListCover(playListName: String, newPlayListCover: String)

    @Query("update userPLayLists set playLisLastUpdate=:lastUpdated where playListName=:playListName")
    suspend fun updatePlayListLastUpdatedDate(playListName: String, lastUpdated: String)

    @Query("select playListName from userPLayLists")
    suspend fun getAllPlaylistsNames(): List<String>


    @Query("update userPLayLists set playListItems =:items")
    fun upadtePlayListItemsByItsName(items: List<Item>)


}