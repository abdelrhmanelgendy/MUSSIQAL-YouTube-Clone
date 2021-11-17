package com.example.musiqal.userPLaylists.mvi.viewStates

import com.example.musiqal.userPLaylists.model.UserPlayList

sealed class ListOfUserPlayListsState {
    class Success(
        val listOfPlayLists: List<UserPlayList>,
        ) : ListOfUserPlayListsState()

    object Idel : ListOfUserPlayListsState()
    object Loading : ListOfUserPlayListsState()
    class Failed(val errorMessgae: String) : ListOfUserPlayListsState()
}
