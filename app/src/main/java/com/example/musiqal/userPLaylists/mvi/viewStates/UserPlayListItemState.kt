package com.example.musiqal.userPLaylists.mvi.viewStates

import com.example.musiqal.userPLaylists.model.UserPlayList

sealed class UserPlayListItemState {
    class Success(
        val listOfPlayLists: UserPlayList,
        ) : UserPlayListItemState()

    object Idel : UserPlayListItemState()
    object Loading : UserPlayListItemState()
    class Failed(val errorMessgae: String) : UserPlayListItemState()
}
