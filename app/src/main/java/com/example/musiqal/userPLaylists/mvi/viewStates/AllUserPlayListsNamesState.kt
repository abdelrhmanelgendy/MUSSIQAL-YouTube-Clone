package com.example.musiqal.userPLaylists.mvi.viewStates

sealed class AllUserPlayListsNamesState {
    class Success(
        val listOfNames:List<String>,
        ) : AllUserPlayListsNamesState()

    object Idel : AllUserPlayListsNamesState()
    object Loading : AllUserPlayListsNamesState()
    class Failed(val errorMessgae: String) : AllUserPlayListsNamesState()
}
