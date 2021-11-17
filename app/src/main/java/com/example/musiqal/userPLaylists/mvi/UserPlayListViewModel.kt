package com.example.musiqal.userPLaylists.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiqal.userPLaylists.model.UserPlayList
import com.example.musiqal.userPLaylists.mvi.viewStates.AllUserPlayListsNamesState
import com.example.musiqal.userPLaylists.mvi.viewStates.ListOfUserPlayListsState
import com.example.musiqal.userPLaylists.mvi.viewStates.UserPlayListItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPlayListViewModel @Inject constructor(
    val userPlayListsMainRepository:
    UserPlayListsMainRepository
) : ViewModel() {

    private val _listOfUserPLayLists: MutableStateFlow<ListOfUserPlayListsState> =
        MutableStateFlow(ListOfUserPlayListsState.Idel)
    var listOfUserPLayListsStateFLow: StateFlow<ListOfUserPlayListsState> = _listOfUserPLayLists


    private val _userPLayListItem: MutableStateFlow<UserPlayListItemState> =
        MutableStateFlow(UserPlayListItemState.Idel)
    var userPLayListItem: StateFlow<UserPlayListItemState> = _userPLayListItem


    private val _userPLayListsNames: MutableStateFlow<AllUserPlayListsNamesState> =
        MutableStateFlow(AllUserPlayListsNamesState.Idel)
    val userPLayListsNames: StateFlow<AllUserPlayListsNamesState> = _userPLayListsNames

    fun updatePlayListCover(newPLayListCover: String, playLisName: String) {
        viewModelScope.launch {
            userPlayListsMainRepository.updatePlayListCover(
                playListName = playLisName,
                newPlayListCover = newPLayListCover
            )
        }

    }

    fun updatePLayListLastUpdate(lastUpdateDate: String, playLisName: String) {
        viewModelScope.launch {
            userPlayListsMainRepository.updatePlayListLastUpdatedDate(
                playListName = playLisName,
                lastUpdated = lastUpdateDate
            )
        }

    }

    fun insertNewPLayList(userPlayList: UserPlayList) {
        viewModelScope.launch {
            userPlayListsMainRepository.insertNewPLayList(userPlayList)
        }
    }

    fun deletePlayListByName(playLisName: String) {
        viewModelScope.launch {
            userPlayListsMainRepository.deletePlayListByName(playLisName)
        }
    }

    fun getAllPlayLists() {
        Log.d("TAG", "getAllPlayLists: ")
        _listOfUserPLayLists.value = ListOfUserPlayListsState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                val allPlayLists = userPlayListsMainRepository.getAllPlayLists()
                _listOfUserPLayLists.value = ListOfUserPlayListsState.Success(allPlayLists)

            }
                .onFailure {
                    throw Exception(it.message)
                    _listOfUserPLayLists.value = ListOfUserPlayListsState.Failed(it.message!!)
                }

        }
    }

    fun getPlayListByName(playLisName: String) {

        _userPLayListItem.value = UserPlayListItemState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                val allPlayLists = userPlayListsMainRepository.getPlayListByItsName(playLisName)
                _userPLayListItem.value = UserPlayListItemState.Success(allPlayLists)

            }
                .onFailure {
                    _userPLayListItem.value = UserPlayListItemState.Failed(it.message!!)
                }

        }
    }
    fun getAllPlayListsNames()
    {

        _userPLayListsNames.value = AllUserPlayListsNamesState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                val allPlayLists = userPlayListsMainRepository.getALlPlayListsNames()
                Log.d("TAG", "getAllPlayListsNames: ")
                _userPLayListsNames.emit(AllUserPlayListsNamesState.Success(allPlayLists))

            }
                .onFailure {
                    _userPLayListsNames.emit( AllUserPlayListsNamesState.Failed(it.message!!))
                }

        }
    }




}